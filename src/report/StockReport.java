package report;

public class StockReport extends Document implements Reportable {

    public StockReport(String title) {
        super(title);
    }

    @Override
    public void generateReport() {

        System.out.println("Generating Stock Report...");
    }

    @Override
    public void printDocument() {

        System.out.println("Printing Stock Report...");
    }
}