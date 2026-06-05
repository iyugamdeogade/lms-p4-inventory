package report;

public class StockReport extends Document implements Reportable {

    public StockReport(String title) {
        super(title);
    }

    @Override
    public void generateReport() {
        System.out.println("=== " + title + " ===");
        System.out.println("Available Reports:");
        System.out.println("  > Menu Option 3  : Low Stock Alert (console)");
        System.out.println("  > Menu Option 4  : Generate Low Stock HTML Report");
        System.out.println("  > Menu Option 6  : Category-Wise Stock Report (console)");
        System.out.println("  > Menu Option 12 : Generate Stock Summary HTML Report");
    }

    @Override
    public void printDocument() {
        System.out.println("============================================");
        generateReport();
        System.out.println("============================================");
    }
}
