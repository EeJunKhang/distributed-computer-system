Remember to modify 
 - `src\config\DatabaseProperties.properties`
 - `src\config\NetworkProperties.properties`

Then Recreate MySQL database with
- `src\config\food_ordering_database.sql`

Then Clean & Build w/ Netbeans before running Client 

Add the following Run Config for SSL output 
- `-Djavax.net.debug=ssl:handshake`