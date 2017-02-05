# Introduction
This is a simple application that allows registered user to send messages to to other users. User can see received and sent messages, search received
messages and change account password.
The project is available at https://github.com/snabu/cybersecurity-base
## Running the program
Clone the project and open in IDE (tested with Netbeans and IntelliJ) and run

The application is available at http://localhost:8080

There are two predefined users available:
username: testuser1, password: 12345
username: testuser2, password: 12345

# Finding vulnerabilities
Running OWASP ZAP first as a proxy and then running active scan with user correctly configured we get alerts for
* SQL injection in search form
* Cookie no HTTPOnly flag
* Cross Site Scripting

We also notice that the application has CSRF vulnerability by creating anti-CSRF test form for POST:changepassword and trying it. 

Lets now look findings closer and see what else we can find.
First login to application using testuser1

### Issue 1 and 2: Broken session management (A2) and XSS (A3)
* select "crete message" 
* recipient: testuser2, subject xss, message  `<script>alert(document.cookie);</script>` 
* using different browser login as testuser2, go to messages, select message "xss", alert window pops up showing JSESSIONID. So XSS works and we also notice
that the sessionID is predictable resulting __A2__   

fix: SessionId generation is defined in file TomcatCustomConfiguration. Remove the file from project as it is not needed. Removing the file also sets session 
cookie http-only by default. To fix XSS vulnerability open file messageview.html replace  `th:utext ` with  `th:text `, additionally can check input data in function 
 `submitForm(...) ` in file MessageController

### Issue 3 CSRF (A8)
We noticed earlier that the application has CSRF culnerability
fix: in SecurityConfiguration.java delete line   `http.csrf().disable(); ` Password change should also be modified to check the current password before changing the
password

### Issue 4 and 5 SQL Injection (A1) and passwords stored in plaintext (A6)
* Go to home->search
* based on page source we make educated guess that the query requires two columns
* enter  `test' UNION SELECT table_name, table_name FROM information_schema.tables;-- ` and click search, we can now see all table names so application
has SQL injection vulnerability __(A1)__. Notice table  `Account `
* based on login form we make educated guess that table account might contain username and password columns
* enter  `test' UNION SELECT username,password FROM account;-- ` to search field and press search.Search returns list of 
usernames and passwords in plaintext resulting __(A6)__.

Fix: in  `SearchController.java ` modify function  `String search(...) ` and replace with parametrized query or use jpa queries. In  `SecurityConfiguration.java ` 
remove  `PlaintextPasswordEncoder ` and enable proper passwordencoder

### Issue 6 Insecure Direct object references (A4)
* create new user and log in
* go to messages, no messages sent or received
* change URL to  `http://localhost:8080/messages/view/1 `
* you can now see message from testuser1 to testuser2

Fix: in MessageController.java modify function showMessage to check that the user is either message sender or recipient __(A4)__


### Issue 7 Misconfiguration (A5)
* Enter url http://localhost:8080/h2-console and notice that the h2-console is enabled __(A5)__
 
Fix: remove h2-console from  `authorizeRequests.antMatchers() ` in file  `SecurityConfiguration.java `, remove also  `http.headers().frameOptions().disable(); ` 
set  `spring.h2.console.enabled = false ` in  `appliction.properties` file. Also default pwd should be changed 

### Issue 8 bonus (A4, A5)
* Go to view message, select message and check the source of resulting page. There's commented out piece of HTML for modify message functionality
* Using the endpoint defined
in commented out code we can modify any message as long as logged in.

Fix: remove modify functionality from MessageController.java, if modify functionality is needed it must include proper checking for authorized user.

### Other issues
* Default error pages should be replaced with more suitable error pages
* Input lengths should be checked in create message and change password functions
