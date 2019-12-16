public class User implements java.io.Serializable{  
    private String username; 
    private String password;
    private boolean admin;  
//getters and setters 
	public User(String username,String password){
		this.username=username;
		this.password=password;
	} 
    public String getUsername(){
    	return username;
    }
    public String getPassword(){
    	return password;
    }
    public void setUsername(){
    	this.username=username;
    }
    public void setPassword(){
    	this.password=password;
    }
    public void setAdmin(){
    	this.admin=true;
    }
    public void deleteAdmin(){
    	this.admin=false;
    }
}  