import smtplib
import sys
 
server = smtplib.SMTP('smtp.gmail.com', 587)
server.starttls()
server.login("superpain@gmail.com", "amis141449")

msg = sys.argv[1]
server.sendmail("superpain@gmail.com", "jaewoopa@gmail.com", msg)
server.quit()