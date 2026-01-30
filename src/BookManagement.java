import java.util.*;
import java.util.stream.*;

public class BookManagement {
    
    public static void main(String[] args) {
        List<Book> listBook = new ArrayList<>();
        Scanner x = new Scanner(System.in);
        
        // Menu chương trình
        while (true) {
            System.out.println("\n========== QUẢN LÝ SÁCH ==========");
            System.out.println("1. Thêm 1 cuốn sách");
            System.out.println("2. Xóa 1 cuốn sách");
            System.out.println("3. Thay đổi cuốn sách");
            System.out.println("4. Xuất thông tin tất cả các cuốn sách");
            System.out.println("5. Tìm cuốn sách có tựa đề chứa chữ 'Lập trình' và không phân biệt hoa thường");
            System.out.println("6. Lấy sách: Nhập vào 1 số K và giá sách P mong muốn tìm kiếm");
            System.out.println("7. Nhập vào 1 danh sách các tác giả từ bàn phím");
            System.out.println("0. Thoát chương trình");
            System.out.print("Nhập lựa chọn của bạn: ");
            
            int chon = x.nextInt();
            x.nextLine(); // Đọc bỏ dòng thừa
            
            switch (chon) {
                case 1: { // Thêm 1 cuốn sách
                    System.out.println("\n=== THÊM SÁCH MỚI ===");
                    Book newBook = new Book();
                    newBook.input();
                    listBook.add(newBook);
                    System.out.println("Đã thêm sách thành công!");
                    break;
                }
                
                case 2: { // Xóa 1 cuốn sách
                    System.out.print("\nNhập mã sách cần xóa: ");
                    int idXoa = x.nextInt();
                    boolean removed = listBook.removeIf(b -> b.getId() == idXoa);
                    if (removed) {
                        System.out.println("Đã xóa sách thành công!");
                    } else {
                        System.out.println("Không tìm thấy sách có mã: " + idXoa);
                    }
                    break;
                }
                
                case 3: { // Thay đổi cuốn sách
                    System.out.print("\nNhập mã sách cần thay đổi: ");
                    int idSua = x.nextInt();
                    x.nextLine();
                    
                    Book bookToUpdate = null;
                    for (Book b : listBook) {
                        if (b.getId() == idSua) {
                            bookToUpdate = b;
                            break;
                        }
                    }
                    
                    if (bookToUpdate != null) {
                        System.out.println("Nhập thông tin mới cho sách:");
                        System.out.print("Tên sách mới: ");
                        bookToUpdate.setTitle(x.nextLine());
                        System.out.print("Tác giả mới: ");
                        bookToUpdate.setAuthor(x.nextLine());
                        System.out.print("Giá mới: ");
                        bookToUpdate.setPrice(x.nextDouble());
                        System.out.println("Đã cập nhật sách thành công!");
                    } else {
                        System.out.println("Không tìm thấy sách có mã: " + idSua);
                    }
                    break;
                }
                
                case 4: { // Xuất thông tin tất cả các cuốn sách
                    System.out.println("\n=== DANH SÁCH TẤT CẢ SÁCH ===");
                    if (listBook.isEmpty()) {
                        System.out.println("Danh sách sách trống!");
                    } else {
                        System.out.printf("%-10s %-30s %-20s %10s\n", "Mã sách", "Tên sách", "Tác giả", "Đơn giá");
                        System.out.println("--------------------------------------------------------------------------------");
                        listBook.forEach(Book::output);
                    }
                    break;
                }
                
                case 5: { // Tìm cuốn sách có tựa đề chứa chữ "Lập trình"
                    System.out.println("\n=== TÌM SÁCH CHỨA 'LẬP TRÌNH' ===");
                    List<Book> list5 = listBook.stream()
                            .filter(b -> b.getTitle().toLowerCase().contains("lập trình"))
                            .collect(Collectors.toList());
                    
                    if (list5.isEmpty()) {
                        System.out.println("Không tìm thấy sách nào!");
                    } else {
                        System.out.printf("%-10s %-30s %-20s %10s\n", "Mã sách", "Tên sách", "Tác giả", "Đơn giá");
                        System.out.println("--------------------------------------------------------------------------------");
                        list5.forEach(Book::output);
                    }
                    break;
                }
                
                case 6: { // Lấy K cuốn sách đầu tiên có giá <= P
                    System.out.print("\nNhập số lượng sách K: ");
                    int k = x.nextInt();
                    System.out.print("Nhập giá sách P: ");
                    double p = x.nextDouble();
                    
                    List<Book> list6 = listBook.stream()
                            .filter(b -> b.getPrice() <= p)
                            .limit(k)
                            .collect(Collectors.toList());
                    
                    System.out.println("\n=== " + k + " SÁCH ĐẦU TIÊN CÓ GIÁ <= " + p + " ===");
                    if (list6.isEmpty()) {
                        System.out.println("Không tìm thấy sách nào!");
                    } else {
                        System.out.printf("%-10s %-30s %-20s %10s\n", "Mã sách", "Tên sách", "Tác giả", "Đơn giá");
                        System.out.println("--------------------------------------------------------------------------------");
                        list6.forEach(Book::output);
                    }
                    break;
                }
                
                case 7: { // Nhập danh sách tác giả và lọc sách
                    System.out.print("\nNhập danh sách tác giả (cách nhau bởi dấu phẩy): ");
                    String inputAuthors = x.nextLine();
                    Set<String> authorSet = Arrays.stream(inputAuthors.split(","))
                            .map(String::trim)
                            .collect(Collectors.toSet());
                    
                    List<Book> list7 = listBook.stream()
                            .filter(b -> authorSet.contains(b.getAuthor()))
                            .collect(Collectors.toList());
                    
                    System.out.println("\n=== SÁCH CỦA CÁC TÁC GIẢ: " + authorSet + " ===");
                    if (list7.isEmpty()) {
                        System.out.println("Không tìm thấy sách nào!");
                    } else {
                        System.out.printf("%-10s %-30s %-20s %10s\n", "Mã sách", "Tên sách", "Tác giả", "Đơn giá");
                        System.out.println("--------------------------------------------------------------------------------");
                        list7.forEach(Book::output);
                    }
                    break;
                }
                
                case 0: { // Thoát
                    System.out.println("\nCảm ơn bạn đã sử dụng chương trình!");
                    x.close();
                    return;
                }
                
                default: {
                    System.out.println("\nLựa chọn không hợp lệ! Vui lòng chọn lại.");
                }
            }
        }
    }
}