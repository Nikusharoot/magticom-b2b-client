ჯავა კლინტისთვის საჭიროა keystore. რომლშიც უნდა განთავსდეს, კლიენტის სერთიფიკატი, კლიენტის private key, და სერვერის სერთიფიკატი.
მაგალითად ავიღოთ Let's Encript ის მიერ ხელმოწერილი სერთიფიკატი, 
CSR რექვესტის დადასტურების შემდეგ ვიღებთ ფაილს  cert.pem da privkey.pem,

** :~$> openssl pkcs12 -export -out playapp.p12 -inkey privkey.pem -in  cert.pem**

მიღებული .p12 შეიძლება დაემატოს ბრაუზერს საკუთარ სერთიფიკატებში, chremes შემთხვევაში 
ვხნით, Settings -> Manage certificates და YOUR CERTIFICATES ჩანართში import ღილაკის მეშვეობით ვტვირთავთ მიღებულ .p12 ფაილს. 
ამის შემდეგ შესაძლებელი იქნება b2b.magticom.ge ზე https არხის გახსნა
და ბრაუზერში swagger ინტერფეისის დათვალიერება,
იმისათვის რომ წვდომადი იყოს მეთოდები საჭიროა თქვენი სერთიფიკატი cert.pem გააგზავნოთ მაგთიკომში რათა მოხდეს მომხმარების რეგისტრაცია.
ჯავა კლიენტისთვის საჭიროა მიღებული *.p12 ფაილი გარდავქმნათ *.jks ფაილად.

** :~$> keytool -importkeystore -srckeystore playapp.p12 -srcstoretype pkcs12  -destkeystore playapp1.jks -deststoretype jks -deststorepass yourPassword**

ამის შემდეგ მიღებულ .jks ფაილში, საჭიროა ჩავამატოთ სერვერის სერთიფიკატი. ამისათვის შეგვიძლია გამოვიყენოთ  კლასი InstallCert,ამ კლასის გაშვებით
ხდება b2b.magticom.ge დან სერთიფიკატის აღება და თქვენს მიერ მითითებულ .jks ფაილში დამატება
ამის შემდეგ SimpleClient   class-ის გაშვებით შეგვიძლია დავრწმუნდეთ რომ ჯავა კლიენტი გაზსნის არხს



genarate *.p12 

** :~$> openssl pkcs12 -export -out playapp.p12 -inkey privkey.pem -in  cert.pem**


generate *.jks
** :~$> keytool -importkeystore -srckeystore playapp.p12 -srcstoretype pkcs12  -destkeystore playapp1.jks -deststoretype jks -deststorepass yourPassword**

alternativly you can set .p12 in jvm system properties

      System.setProperty( "sun.security.ssl.allowUnsafeRenegotiation", "true" );
      System.setProperty("javax.net.ssl.keyStore",pathToYourP12);
      System.setProperty("javax.net.ssl.keyStorePassword",yourP12Password);
