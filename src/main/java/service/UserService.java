package service;

import dao.UserDAO;
import model.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public String searchUser(String username){
        User user = userDAO.findUserByUsername(username);

        if(user == null){
            return "Khong tim thay nguoi dung";
        }else {
            int postCount = userDAO.countPostByUserId(user.getId());
            return "Nguoi dung" + user.getUsername() + "co" + postCount + "bai viet";
        }
    }
}
