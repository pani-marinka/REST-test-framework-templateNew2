package entity;

public class ListOptions {
    public String orderType; // should be moved into Enum, since only 2 values exist: "asc", "desc"
    public int page;
    public boolean pagination;
    public int size;
    public String sortBy;

    // setting some of the default values
    public ListOptions() {
        this.page = 1;
        this.pagination = true;
        this.size = 10;
    }

    public ListOptions setOrderType(String orderType) {
        this.orderType = orderType;
        return this;
    }

    public ListOptions setPage(int page) {
        this.page = page;
        return this;
    }

    public ListOptions setPagination(boolean pagination) {
        this.pagination = pagination;
        return this;
    }

    public ListOptions setSize(int size) {
        this.size = size;
        return this;
    }

    public ListOptions setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }
}
