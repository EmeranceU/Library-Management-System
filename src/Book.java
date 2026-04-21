public class Book {
        private int id;
        private String title;
        private String author;
        private boolean isAvailable;
        private Member borrowedBy;

        public Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.isAvailable = true;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }
        public void setBorrowedBy(Member member) {
            this.borrowedBy = member;
        }
    }
