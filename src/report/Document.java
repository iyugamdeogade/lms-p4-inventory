package report;

public abstract class Document {

    protected String title;

    public Document(String title) {
        this.title = title;
    }

    public void showTitle() {
        System.out.println("Report Title: " + title);
    }

    public abstract void printDocument();
}