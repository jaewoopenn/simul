import smtplib
 
server = smtplib.SMTP('smtp.gmail.com', 587)
server.starttls()
server.login("superpain@gmail.com", "amissuper")
 
msg = "Alarm test"
server.sendmail("superpain@gmail.com", "jaewoopa@gmail.com", msg)
server.quit()