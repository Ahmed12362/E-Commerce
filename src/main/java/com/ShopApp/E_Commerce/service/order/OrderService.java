package com.ShopApp.E_Commerce.service.order;

import com.ShopApp.E_Commerce.dto.OrderDto;
import com.ShopApp.E_Commerce.enums.OrderStatus;
import com.ShopApp.E_Commerce.exceptions.ResourceNotFoundException;
import com.ShopApp.E_Commerce.model.*;
import com.ShopApp.E_Commerce.repository.OrderRepository;
import com.ShopApp.E_Commerce.repository.ProductRepository;
import com.ShopApp.E_Commerce.response.StripeResponse;
import com.ShopApp.E_Commerce.service.cart.ICartService;
import com.ShopApp.E_Commerce.service.user.UserService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Override
    public StripeResponse placeOrder() throws StripeException {
        User user = userService.getAuthenticatedUser();
        Cart cart = cartService.getCartByUserId(user.getUserId());
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        Stripe.apiKey = secretKey;
//        cart.getCartItems().stream()
//                        .map(cartItem -> {
//                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                    .setName(cartItem.getProduct().getName())
//                                    .build();
//                            return cartItem;
//                        });
        List<SessionCreateParams.LineItem> lineItems = order.getOrderItems().stream()
                .map(orderItem -> SessionCreateParams.LineItem.builder()
                        .setQuantity((long) orderItem.getQuantity())
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("egp")
                                        .setUnitAmount(orderItem.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(orderItem.getProduct().getName())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build())
                .toList();

//        order.getOrderItems().stream()
//                .map(orderItem -> {
//                    SessionCreateParams.LineItem.builder()
//                            .setQuantity( (long)orderItem.getQuantity())
//                            .setPriceData()
//                            .build();
//                    return orderItem;
//                });
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8081/success")
                .setCancelUrl("http://localhost:8081/cancel")
                .addAllLineItem(lineItems)
                .build();
        SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT);
        Session session = Session.create(params);



        cartService.clearCart(cart.getId());
        return StripeResponse.builder()
                .status("SUCCESS")
                .message("Payment Session Created Successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getCartItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );
        }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public Long getAuthenticatedUserId() {
        return userService.getAuthenticatedUser().getUserId();
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
    }

    @Override
    public List<OrderDto> getUserOrders() {
        User user = userService.getAuthenticatedUser();
        List<Order> orders = orderRepository.findByUserUserId(user.getUserId());
        return orders
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public byte[] generateOrderPdf(Order order) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, Color.BLUE);
        Paragraph title = new Paragraph("Order Invoice", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Order Info
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("Order ID: " + order.getOrderId(), infoFont));
        document.add(new Paragraph("Date: " + order.getOrderDate(), infoFont));
        document.add(new Paragraph("Status: " + order.getOrderStatus(), infoFont));
        document.add(new Paragraph("Customer: " + order.getUser().getFirstName() + " " + order.getUser().getLastName(), infoFont));
        document.add(new Paragraph(" "));

        // Table
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        table.setWidths(new float[]{3f, 2f, 1f, 2f});

        // Table Header Style
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Color headerBgColor = new Color(63, 81, 181); // Indigo

        addCellToTable(table, "Product", headerFont, headerBgColor);
        addCellToTable(table, "Brand", headerFont, headerBgColor);
        addCellToTable(table, "Qty", headerFont, headerBgColor);
        addCellToTable(table, "Price", headerFont, headerBgColor);

        // Table Content
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        for (OrderItem item : order.getOrderItems()) {
            table.addCell(new Phrase(item.getProduct().getName(), contentFont));
            table.addCell(new Phrase(item.getProduct().getBrand(), contentFont));
            table.addCell(new Phrase(String.valueOf(item.getQuantity()), contentFont));
            table.addCell(new Phrase(item.getPrice().toString(), contentFont));
        }

        document.add(table);

        // Total Amount
        Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.RED);
        Paragraph total = new Paragraph("Total: " + order.getTotalAmount() + " EGP", totalFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        total.setSpacingBefore(10);
        document.add(total);

        document.close();
        return out.toByteArray();
    }

    private void addCellToTable(PdfPTable table, String text, Font font, Color bgColor) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }


}
