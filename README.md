# Secure Access System 🔐

A Java desktop app that demonstrates **authentication** and **role-based access control (RBAC)**, backed by a MySQL database.

## Features

- **Login with role selection**: Admin, Employee or Visitor
- **Database authentication** through JDBC, using **prepared statements** to block SQL injection
- **Account lockout** after 3 failed login attempts
- **Role-based dashboard**: each role only gets the actions it's allowed

| Action | Visitor | Employee | Admin |
|--------|:------:|:--------:|:-----:|
| View current status | ✅ | ✅ | ✅ |
| Edit status message | ❌ | ✅ | ✅ |
| Access system logs  | ❌ | ❌ | ✅ |

## Tech stack

Java · Swing · JDBC · MySQL

## Project structure

```
Security-Access-System/
├── src/
│   ├── Security.java       # Login window + role-based dashboard
│   └── DBConnection.java   # MySQL connection (settings from environment variables)
├── lib/                    # Put the MySQL JDBC driver .jar here
└── schema.sql              # Creates the database, users table and demo accounts
```

## Run it

1. **Set up the database**
   ```bash
   mysql -u root -p < schema.sql
   ```
2. **Download the MySQL driver** ([MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/), "Platform Independent") and put the `.jar` in `lib/`.
3. **Set your MySQL password** (it's read from an environment variable, never stored in the code)
   ```bash
   export DB_PASS="your-mysql-password"
   ```
4. **Compile and run**
   ```bash
   javac -d out src/*.java
   java -cp "out:lib/*" Security          # Windows: java -cp "out;lib/*" Security
   ```

**Demo logins:** `admin / admin123`, `employee / employee123`, `visitor / visitor123` (pick the matching role).

## Future improvements

- Hash passwords (e.g. bcrypt) instead of storing them in plain text
- Record real login events in an audit log table for the Admin logs view
- Persist the status message in the database
