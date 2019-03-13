package cloud.tiny.tinydrivestarter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tinydrive")
public final class Config {
  private List<User> users = new ArrayList<User>();
  private boolean scopeUser;
  private String apiKey;

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public boolean isScopedUser() {
    return scopeUser;
  }

  public void setScopeUser(boolean scopeUser) {
    this.scopeUser = scopeUser;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  static final class User {
    private String username;
    private String password;
    private String fullname;

    public String getUserName() {
      return this.username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
      return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }
  }
}