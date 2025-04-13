Remember to modify 
 - `src\config\DatabaseProperties.properties`
 - `src\config\NetworkProperties.properties`

Then Recreate MySQL database with
- `src\config\food_ordering_database.sql`

---
Namtung-Note:
- Database operation through Java (manager classes) shuold be completed. Please notify if there's any issue
  - Test/Verify with `src\database\test\DAOTestSuite.java` or test clients in `src\manager\testclient`
- Implementing SSL (errors in that package can be ignored)