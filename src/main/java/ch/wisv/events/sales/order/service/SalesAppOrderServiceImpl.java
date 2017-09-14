package ch.wisv.events.sales.order.service;

import ch.wisv.events.core.exception.EventsModelNotFound;
import ch.wisv.events.core.exception.EventsSalesAppException;
import ch.wisv.events.core.model.order.Customer;
import ch.wisv.events.core.model.order.Order;
import ch.wisv.events.core.service.customer.CustomerService;
import ch.wisv.events.core.service.order.OrderService;
import org.springframework.stereotype.Service;

/**
 * Copyright (c) 2016  W.I.S.V. 'Christiaan Huygens'
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
@Service
public class SalesAppOrderServiceImpl implements SalesAppOrderService {

    /**
     * Field orderService
     */
    private final OrderService orderService;

    /**
     * Field customerService
     */
    private final CustomerService customerService;

    /**
     * Constructor SalesAppEventServiceImpl creates a new SalesAppEventServiceImpl instance.
     *
     * @param orderService    of type OrderService
     * @param customerService of type CustomerService
     */
    public SalesAppOrderServiceImpl(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    /**
     * Method assertAmountOfProductLeft.
     *
     * @param order    of type Order
     * @param customer of type Customer
     */
    @Override
    public void addCustomerToOrder(Order order, Customer customer) {
        customer = this.customerService.getByRFIDToken(customer.getRfidToken());
        Order that;

        try {
            that = this.orderService.getByReference(order.getPublicReference());
        } catch (EventsModelNotFound e) {
            throw new EventsSalesAppException("Order not found!");
        }

        that.setCustomer(customer);
        this.orderService.update(that);
    }

    /**
     * Method create ...
     *
     * @param order of type Order
     */
    @Override
    public void create(Order order) {
        this.orderService.create(order);
    }

    /**
     * Method update ...
     *
     * @param order of type Order
     */
    @Override
    public void update(Order order) {
        this.orderService.update(order);
    }
}