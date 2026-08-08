# Hostel Complaint App Database Seeder

This folder contains a Java-based seeder for the `complaints` table and helper scripts to run it.

## Files

- `db/seed_complaints.sql` - Creates the `complaints` table and inserts the six sample records.
- `DBSeeder.java` - Java utility that reads the SQL file and executes it against your MySQL database.
- `run_dbseeder.bat` - Windows launcher using `lib/*` on the classpath.
- `run_dbseeder.sh` - Unix/macOS launcher using `lib/*` on the classpath.

## Setup

1. Download the MySQL JDBC driver JAR (for example, `mysql-connector-java-8.0.x.jar`).
2. Create a `lib` folder under `APP`.
3. Place the JDBC JAR in `APP/lib`.

## Compile

From the `APP` folder:

```cmd
javac DBSeeder.java
```

## Run on Windows

```cmd
run_dbseeder.bat jdbc:mysql://localhost:3306/hostel root password
```

## Run on Unix/macOS

```bash
./run_dbseeder.sh jdbc:mysql://localhost:3306/hostel root password
```

## Notes

- Replace `hostel`, `root`, and `password` with your actual database name, username, and password.
- The script uses `lib/*` so any JDBC driver JAR in `APP/lib` will be loaded.
- If your MySQL server uses TLS or a custom port, adjust the JDBC URL accordingly.

## Verify Seeded Records

After running the seeder, verify the six complaints with this SQL:

```sql
SELECT complaint_id, room, category, priority, status
FROM complaints
ORDER BY complaint_id;
```

You should see:

- `HP-1024` | `A-16` | `Electrical` | `High` | `Pending`
- `HP-1019` | `A-16` | `Plumbing` | `Medium` | `In Progress`
- `HP-1008` | `A-16` | `Cleaning/Housekeeping` | `Low` | `Resolved`
- `HP-0996` | `A-16` | `WiFi` | `High` | `Rejected`
- `HP-0988` | `A-16` | `Furniture` | `Medium` | `Resolved`
- `HP-0977` | `A-16` | `Mess` | `Low` | `Pending`
