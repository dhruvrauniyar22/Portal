
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBSeeder {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java DBSeeder <jdbc-url> <db-user> <db-pass>");
            System.out.println("Example: java DBSeeder jdbc:mysql://localhost:3306/hostel root password");
            return;
        }
        String url = args[0];
        String user = args[1];
        String pass = args[2];

        Path sql = Path.of("db/seed_complaints.sql");
        if (!Files.exists(sql)) {
            System.err.println("Seed file not found: " + sql.toAbsolutePath());
            return;
        }

        String sqlContent = Files.readString(sql);

        try (Connection conn = DriverManager.getConnection(url, user, pass); Statement st = conn.createStatement()) {
            // split on semicolon for basic multi-statement
            String[] parts = sqlContent.split(";");
            for (String part : parts) {
                String s = part.trim();
                if (s.isEmpty()) {
                    continue;
                }
                st.execute(s);
            }
            System.out.println("Database seeded successfully.");
        }
    }
}
