package io.summerx.framework.core.pagination;

import java.util.Arrays;
import java.util.Set;

public class OrderablePagination extends Pagination {

    private Set<PageOrder> orders;

    public OrderablePagination() {
        super();
    }

    public OrderablePagination(int pageSize) {
        super(pageSize);
    }

    public OrderablePagination(int pageSize, int pageIndex) {
        super(pageSize, pageIndex);
    }

    public void addOrder(PageOrder... orders) {
        if (orders == null || orders.length == 0) {
            return;
        }
        this.orders.addAll(Arrays.asList(orders));
    }

    public Set<PageOrder> getOrders() {
        return orders;
    }
}
