1.Correr MulticastServer.java
2.Correr UserServer.java
3.Correr UserServerBackup.java
4.Correr Client.java

Caso o Cliente e o Servidor RMI(UserServer) estejam em computadores diferentes pode ser preciso alterar o IP que � usado para ligar ao RMI.
Para isso:
1.Abrir o c�digo-fonte do Client.java
2.Alterar a vari�vel SERVER_IP que se encontra na classe do servidor para o IP do computador que est� a correr o UserServer.java