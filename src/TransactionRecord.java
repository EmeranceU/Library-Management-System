public class TransactionRecord {

    private final String memberName;
    private final String bookTitle;
    private final String action;
    private final String timestamp;

    public TransactionRecord(String memberName, String bookTitle, String action, String timestamp) {
        this.memberName = memberName;
        this.bookTitle = bookTitle;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAction() {
        return action;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
