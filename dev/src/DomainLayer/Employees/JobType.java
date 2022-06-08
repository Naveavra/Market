package DomainLayer.Employees;

public enum JobType {
    HR_MANAGER, SHIFT_MANAGER ,CASHIER, STOCK_KEEPER, DRIVER, MERCHANDISER, LOGISTICS_MANAGER,
    TRANSPORT_MANAGER, STORE_MANAGER;


    public static JobType get(String job) {
        switch (job) {
            case "HR_MANAGER":
                return HR_MANAGER;
            case "SHIFT_MANAGER":
                return SHIFT_MANAGER;
            case "CASHIER":
                return CASHIER;
            case "STOCK_KEEPER":
                return STOCK_KEEPER;
            case "DRIVER":
                return DRIVER;
            case "MERCHANDISER":
                return MERCHANDISER;
            case "LOGISTICS_MANAGER":
                return LOGISTICS_MANAGER;
            case "TRANSPORT_MANAGER":
                return TRANSPORT_MANAGER;
            case "STORE_MANAGER":
                return STORE_MANAGER;
        }
        return null;
    }
}
