package com.grizzly.usermicro;

import com.grizzly.usermicro.admin.Admin;
import com.grizzly.usermicro.admin.AdminRepository;
import com.grizzly.usermicro.customer.Customer;
import com.grizzly.usermicro.customer.CustomerDTO;
import com.grizzly.usermicro.customer.CustomerRepository;
import com.grizzly.usermicro.orderitem.OrderItem;
import com.grizzly.usermicro.orderitem.OrderItemDTO;
import com.grizzly.usermicro.orderitem.OrderItemRepository;
import com.grizzly.usermicro.orders.Order;
import com.grizzly.usermicro.orders.OrderDTO;
import com.grizzly.usermicro.orders.OrderRepository;
import com.grizzly.usermicro.user.User;
import com.grizzly.usermicro.user.UserDTO;
import com.grizzly.usermicro.vendor.Vendor;
import com.grizzly.usermicro.vendor.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.grizzly.grizlibrary.helpers.Helper.makeListFromIterable;

@Service
public class UserService {
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public ArrayList<Customer> getAllCustomers(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(customerRepository.findAll(request));
    }


    public ArrayList<Vendor> getAllVendors(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(vendorRepository.findAll(request));
    }


    public ArrayList<Admin> getAllAdmins(Integer pageIndex, String column_name) {
        PageRequest request = getPageRequest(pageIndex, column_name);
        return makeListFromIterable(adminRepository.findAll(request));
    }

    // Lets find the user...
    // Start with admin -> vendor -> customer.
    public User findByUserEmail(String email) {
        // Check admin repo.
        User userFound = adminRepository.findByUserEmail(email);

        // Found!? Great. return it.
        if (userFound != null) {
            userFound.setRole("admin");
            return userFound;
        }

        // Not found... check vendor
        userFound = vendorRepository.findByUserEmail(email);

        // We found it now? return it.
        if (userFound != null) {
            userFound.setRole("vendor");
            return userFound;
        }

        // eugh. still haven't found the user.
        userFound = customerRepository.findByUserEmail(email);

        // mustve found a customer... right ???
        if (userFound != null) {
            userFound.setRole("customer");
            return userFound;
        }

        // found nothing. user doesn't exist
        return null;
    }

    /**
     * Utility function to generate a pagerequest to tell the database how to page and sort a query
     * @param column_name, the fieldname in the database to sort the list
     * @return pageRequest to the method called
     */
    public PageRequest getPageRequest(Integer pageIndex, String column_name) {
        final String[] fields = {"userId", "name", "contact_num", "email"};
        String sortField;
        if (Arrays.asList(fields).contains(column_name)) {
            sortField = column_name;
        } else {
            sortField = "userId";
        }

        Sort sort = new Sort(Sort.Direction.ASC, sortField);

        PageRequest request = PageRequest.of(pageIndex, 25, sort);
        return request;
    }

    /**
     * Get a single item from customer id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of User objs whose names or IDs
     */
    public ArrayList<Customer> getSingleUserCustomer(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                customerRepository.findByUserCustomerId(search, request)
        );
    }

    /**
     * Get a single item from vendor id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of Vendor objs whose names or IDs
     */
    public ArrayList<Vendor> getSingleUserVendor(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                vendorRepository.findByUserVendorId(search, request)
        );
    }

    /**
     * Get a single item from user id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of User objs whose names or IDs
     */
    public ArrayList<Admin> getSingleUserAdmin(Integer search) {
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 1, sort);

        return makeListFromIterable(
                adminRepository.findByUserAdminId(search, request)
        );
    }

    public User addOrUpdateUser(CustomerDTO newUser) {
        try {
            // find the existing Customer
            User user = findByUserEmail(newUser.getEmail());
            if (user != null) {
                User saved;
                if (user.getRole().equals("admin")) {
                    Admin adminUser = (Admin) user;
                    adminUser.setName(newUser.getName());
                    saved = adminRepository.save(adminUser);
                } else if (user.getRole().equals("vendor")) {
                    Vendor vendorUser = (Vendor) user;
                    vendorUser.setName(newUser.getName());
                    saved = vendorRepository.save(vendorUser);
                } else {
                    Customer customerUser = (Customer) user;
                    CustomerDTO existingUser = (CustomerDTO) newUser;
                    customerUser.setContact_num(existingUser.getContact_num());
                    customerUser.setAddress(existingUser.getAddress());
                    saved = customerRepository.save(customerUser);
                }
                return saved;
            } else {
                Customer newCustomer = new Customer();
                CustomerDTO details = (CustomerDTO) newUser;
                newCustomer.setName(details.getName());
                newCustomer.setEmail(details.getEmail());
                newCustomer.setAddress(details.getAddress());
                newCustomer.setContact_num(details.getContact_num());
                User created = customerRepository.save(newCustomer);
                return created;
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Make an ArrayList of Objects based on a passed-in Iterable
     * @param iter An Iterable of Objects
     * @return An ArrayList made from the Iterable
     */
    public static <T> ArrayList<T> makeListFromIterable(Iterable<T> iter) {
        ArrayList<T> list = new ArrayList<T>();

        for(T item: iter) {
            list.add(item);
        }

        return list;
    }


    // CUSTOMER ORDER HISTORY
    /**
     * Get a single order from customer id.
     * @param userId, the string to match to ID to filter the orders by
     * @return ArrayList of Users with Orders
     */
    // 1. Call this instead of normal getSingleUserCustomer when you want their order history
    public CustomerDTO getSingleCustomerWithOrders(Integer userId) {
        CustomerDTO customer = getSingleOrder(userId).get(0);

        return customer;
    }

    /**
     * Get a single order from user id.
     * @param search, the string to match to ID to filter the user by
     * @return ArrayList of User objs whose names or IDs
     */
    // 2. It then matches your user ID with an existing customer
    public ArrayList<CustomerDTO> getSingleOrder(Integer search) {
        ArrayList<CustomerDTO> result = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.ASC, "userId");
        PageRequest request = PageRequest.of(0, 25, sort);

        CustomerDTO customer = customerToDTO(customerRepository.findByUserCustomerId(search, request).get(0));

        result.add(customer);

        return result;
    }

    /**
     * Convert a Customer object into a CustomerDTO
     * @param customer, A Customer obj to get orders for
     * @return A Customer DTO with a populated OrderDTO
     */
    // 3. We now prepare to return your customerDTO
    public CustomerDTO customerToDTO(Customer customer) {

        // 3.1 Check whether your user has made orders in the user_order table
        List<Order> orders = orderRepository.findByUserId(customer.getUserId());

        // 3.2 For each order we find return a OrderDTO
        ArrayList<OrderDTO> orderDTO = new ArrayList<OrderDTO>();

        for (int i = 0; i < orders.size(); i++) {
            String txnId = orders.get(i).getTxn_id();
            Double cost = orders.get(i).getCost();
            String destination = orders.get(i).getDeparting_location();
            LocalDate getShippedOn = orders.get(i).getShipped_on();
            Integer userId = orders.get(i).getUser_id();

            OrderDTO order = new OrderDTO();
            order.setTxn_id(txnId);
            order.setCost(cost);
            order.setDeparting_location(destination);
            order.setShipped_on(getShippedOn);
            // 3.3 And also call getOrder to get the items in the order too
            order.setOrderItemDTO(getOrderItemDTO(orders.get(i)));
            order.setUser_id(userId);

            orderDTO.add(i, order);
        }

        CustomerDTO customerDTO = new CustomerDTO(customer.getUserId(), customer.getAddress(), orderDTO);
        customerDTO.setUserId(customer.getUserId());

        return customerDTO;
    }

    /**
     * Get a List of items for an order
     * @param order, the order id to get items for
     * @return List of OrderItem objs
     */
    // Based on the order ID, we put the items into a OrderItemDTO
    public ArrayList<OrderItemDTO> getOrderItemDTO(Order order) {
        List<OrderItem> items = orderItemRepository.findItemsByOrderId(order.getOrder_id());

        ArrayList<OrderItemDTO> orderItemDTO = new ArrayList<OrderItemDTO>();

        for (int i = 0; i < items.size(); i++) {
            Integer orderId = items.get(i).getOrder_id();
            Integer productId = items.get(i).getProductId();
            Float rating = items.get(i).getRating();
            Integer quantity = items.get(i).getQuantity();

            OrderItemDTO orderItem = new OrderItemDTO();
            orderItem.setOrder_id(orderId);
            orderItem.setProductId(productId);
            orderItem.setRating(rating);
            orderItem.setQuantity(quantity);

            orderItemDTO.add(i, orderItem);
        }

        return orderItemDTO;
    }

    public void addOrder(OrderDTO orderDTO){

        Order newOrder = orderDTO.toEntity();
        orderRepository.save(newOrder);

        Integer ordID = newOrder.getOrder_id();
        ArrayList<OrderItemDTO> OrderItems = orderDTO.getOrderItemDTO();

        for (int i = 0; i < OrderItems.size(); i++ ){
            OrderItem currentItem = OrderItems.get(i).toEntity(ordID);
            currentItem.setOrder_id(ordID);
            currentItem.setRating(Float.parseFloat("5"));

            orderItemRepository.save(currentItem);
        }


    }
}
