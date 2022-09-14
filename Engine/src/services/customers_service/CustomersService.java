package services.customers_service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomersService {
    private Map<String, Customer> customersMap;

    public CustomersService() {

        this.customersMap = new HashMap<>();
    }

    public CustomersService(CustomersService customersService) {
        this.customersMap = new HashMap<>();
        customersService.getAllCustomersInfo().forEach(customer -> {
            Customer cust = new Customer(customer);
            this.customersMap.put(cust.getName(), cust);
        });
    }

    public List<Customer> getAllCustomersInfo() {
        return this.customersMap.values().stream().collect(Collectors.toList());}

    public void addCustomer(String name, int balance) {
        this.customersMap.put(name, new Customer(name, balance));
    }

    public Customer findCustomer(String name){return this.customersMap.get(name);}

    public void sendNotificationToCustomer(String customerName, String loanId, int yaz, float totalAmount) {
        findCustomer(customerName).addNotification(loanId, yaz, totalAmount);
    }
}
