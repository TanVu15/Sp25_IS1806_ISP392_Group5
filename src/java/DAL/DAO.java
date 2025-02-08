/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import java.sql.Connection;
import Model.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class DAO {

    public static final DAO INSTANCE = new DAO();
    protected Connection connect;

    public DAO() {
        connect = new DBContext().connect;
    }

    public static long millis = System.currentTimeMillis();
    public static Date today = new Date(millis);

    public void Register(Users user, int userid) {
        String sql = "INSERT INTO Users (Username, passwordhash, roleid, CreateAt, CreateBy, isDelete) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setInt(3, user.getRoleid());
            ps.setDate(4, today);
            ps.setInt(5, userid);
            ps.setInt(6, 0);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public ArrayList<Users> getUsers() {
        ArrayList<Users> users = new ArrayList<>();
        String sql = "SELECT * "
                + "FROM Users ";
        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                Users u = new Users();
                u.setID(rs.getInt("ID"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("passwordhash"));
                u.setRoleid(rs.getInt("roleid"));
                u.setFullName(rs.getString("FullName"));
                u.setCreateAt(rs.getDate("CreateAt"));
                u.setCreateBy(rs.getInt("CreateBy"));
                u.setIsDelete(rs.getInt("isDelete"));
                u.setDeleteBy(rs.getInt("DeleteBy"));
                u.setDeletedAt(rs.getDate("DeletedAt"));
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        }
        return users;
    }

    public void deleteUser(int deleteid, int userid) {
        String sql = "UPDATE Users SET isDelete = ?, DeleteBy = ?, DeletedAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, 1);
            ps.setInt(1, userid);
            ps.setDate(3, today);
            ps.setInt(4, deleteid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Users user) {
        String sql = "UPDATE Users SET passwordhash = ?,FullName = ? , UpdateAt = ? WHERE id = ?";
        try (PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, user.getPasswordHash());
            ps.setString(2, user.getFullName());
            ps.setDate(3, today);
            ps.setInt(4, user.getID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Users getUserByName(String name) throws Exception {
        String query = "SELECT * FROM Users WHERE username=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Users user = new Users();
            user.setID(rs.getInt("ID"));
            user.setUsername(rs.getString("Username"));
            user.setPasswordHash(rs.getString("PasswordHash"));
            user.setRoleid(rs.getInt("Roleid"));
            user.setFullName(rs.getString("FullName"));
            user.setCreateAt(rs.getDate("CreateAt"));
            user.setCreateBy(rs.getInt("CreateBy"));
            user.setIsDelete(rs.getInt("isDelete"));
            user.setDeleteBy(rs.getInt("DeleteBy"));
            user.setDeletedAt(rs.getDate("DeletedAt"));

            return user;
        }
        return null;
    }
    
    //check insert user
    public boolean checkUsernameExists(String username) {
        try {
            String query = "SELECT COUNT(*) FROM Users WHERE username = ?";
            PreparedStatement ps = connect.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Users getUserByID(int ID) throws Exception {
        String query = "SELECT * FROM Users WHERE ID=? ";
        PreparedStatement ps = connect.prepareStatement(query);
        ps.setInt(1, ID);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            Users user = new Users();
            user.setID(rs.getInt("ID"));
            user.setUsername(rs.getString("Username"));
            user.setPasswordHash(rs.getString("PasswordHash"));
            user.setRoleid(rs.getInt("Roleid"));
            user.setFullName(rs.getString("FullName"));
            user.setCreateAt(rs.getDate("CreateAt"));
            user.setCreateBy(rs.getInt("CreateBy"));
            user.setIsDelete(rs.getInt("isDelete"));
            user.setDeleteBy(rs.getInt("DeleteBy"));
            user.setDeletedAt(rs.getDate("DeletedAt"));

            return user;
        }
        return null;
    }
    
    public ArrayList<Users> getUsersBySearch(String information) {
    ArrayList<Users> users = new ArrayList<>();
    String sql = "SELECT * FROM Users"; // Lấy toàn bộ dữ liệu từ bảng Users
    
    try (PreparedStatement statement = connect.prepareStatement(sql);
         ResultSet rs = statement.executeQuery()) {

        while (rs.next()) {
            Users u = new Users();
            u.setID(rs.getInt("ID"));
            u.setUsername(rs.getString("username"));
            u.setPasswordHash(rs.getString("passwordhash"));
            u.setRoleid(rs.getInt("roleid"));
            u.setFullName(rs.getString("FullName"));
            u.setCreateAt(rs.getDate("CreateAt"));
            u.setCreateBy(rs.getInt("CreateBy"));
            u.setIsDelete(rs.getInt("isDelete"));
            u.setDeleteBy(rs.getInt("DeleteBy"));
            u.setDeletedAt(rs.getDate("DeletedAt"));

            // Chuyển thông tin của user thành một chuỗi
            String userData = (u.getUsername() + " " + 
                               u.getPasswordHash() + " " + 
                               u.getRoleid() + " " + 
                               u.getFullName() + " " + 
                               u.getCreateAt() + " " + 
                               u.getCreateBy() + " " + 
                               u.getIsDelete() + " " + 
                               u.getDeleteBy() + " " + 
                               u.getDeletedAt()).toLowerCase(); // Chuyển về chữ thường để tránh phân biệt hoa/thường

            // Kiểm tra nếu information xuất hiện trong bất kỳ trường nào của user
            if (userData.contains(information.toLowerCase())) {
                users.add(u);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return users;
}
        


//    public ArrayList<Users> getUsers() {
//        ArrayList<Users> users = new ArrayList<>();
//        String sql = "SELECT * "
//                + "FROM Users where roleid = 1 ";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//
//            while (rs.next()) {
//                Users u = new Users();
//
//                u.setUsername(rs.getString("username"));
//                u.setPasswordHash(rs.getString("passwordhash"));
//
//                u.setRoleid(rs.getInt("roleid"));
//                users.add(u);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return users;
//    }
//    public Profile getProfileById(int userid) {
//        Profile profile = null;
//        String sql = "SELECT p.profileid, p.name, p.age, p.diachi, p.phone_number, u.userid "
//                + "FROM Profile p "
//                + "INNER JOIN Users u ON p.userid = u.userid "
//                + "WHERE u.userid = ?";
//
//        try (
//                PreparedStatement statement = connect.prepareStatement(sql)) {
//            // Set the userid parameter in the PreparedStatement
//            statement.setInt(1, userid);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    int profileid = rs.getInt("profileid");
//                    String name = rs.getString("name");
//                    int age = rs.getInt("age");
//                    String address = rs.getString("diachi");
//                    String phone_number = rs.getString("phone_number");
//
//                    Users u = new Users();
//                    u.setUserid(rs.getInt("userid"));
//
//                    profile = new Profile(profileid, name, age, address, phone_number, u);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return profile;
//    }
//
//    public ArrayList<Profile> getAllProfiles() {
//        ArrayList<Profile> profiles = new ArrayList<>();
//        String sql = "SELECT p.profileid, p.name, p.age, p.diachi, p.phone_number, u.userid "
//                + "FROM Profile p "
//                + "INNER JOIN Users u ON p.userid = u.userid";
//        try (
//                PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//            while (rs.next()) {
//                Profile profile = new Profile();
//                profile.setProfileid(rs.getInt("profileid"));
//                profile.setName(rs.getString("name"));
//                profile.setAge(rs.getInt("age"));
//                profile.setAddress(rs.getString("diachi"));
//                profile.setPhoneNumber(rs.getString("phone_number"));
//
//                Users u = new Users();
//                u.setUserid(rs.getInt("userid"));
//                profile.setUser(u);
//
//                profiles.add(profile);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return profiles;
//    }
//
//    public void updateProfile(Profile profile) {
//        String sql = "UPDATE Profile SET name = ?, age = ?, diachi = ?, phone_number = ?, userid = ? WHERE profileid = ?";
//        try (
//                PreparedStatement statement = connect.prepareStatement(sql)) {
//
//            // Set the parameters for the prepared statement
//            statement.setString(1, profile.getName());
//            statement.setInt(2, profile.getAge());
//            statement.setString(3, profile.getAddress());
//            statement.setString(4, profile.getPhoneNumber());
//            statement.setInt(5, profile.getUser().getUserid());
//            statement.setInt(6, profile.getProfileid());
//
//            // Execute the update
//            statement.executeUpdate(); // No need to check for rows updated if returning void
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle the exception (e.g., log it)
//            // You may want to throw a custom exception or handle it as needed
//        }
//    }
//
//    public int getNextProfileId() throws SQLException {
//        int nextProfileId = 1;
//        String sql = "SELECT MAX(profileid) FROM Profile ";
//
//        try (PreparedStatement stmt = connect.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
//            if (rs.next()) {
//                nextProfileId = rs.getInt(1) + 1;
//            }
//        }
//        return nextProfileId;
//
//    }
//
//    public void insertProfile(Profile profile) throws SQLException {
//        String sql = "INSERT INTO Profiles (profileid, name, age, address, phone_number, userid) VALUES (?, ?, ?, ?, ?, ?)";
//
//        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
//
//            stmt.setInt(1, profile.getProfileid());
//            stmt.setNull(2, 0);
//            stmt.setNull(3, 0);
//            stmt.setNull(4, 0);
//            stmt.setNull(5, 0);
//            stmt.setInt(6, profile.getUser().getUserid());
//
//            stmt.executeUpdate();
//        }
//    }
//
//    public void banUser(int userId) {
//        String query = "UPDATE Users SET banned = 1 WHERE userid = ?";
//        try (PreparedStatement stmt = connect.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void unbanUser(int userId) {
//        String query = "UPDATE Users SET banned = 0 WHERE userid = ?";
//        try (PreparedStatement stmt = connect.prepareStatement(query)) {
//            stmt.setInt(1, userId);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public boolean isUserBanned(Users user) throws SQLException {
//        boolean banned = false;
//        String sql = "SELECT banned FROM Users WHERE userid = ?";
//
//        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
//            stmt.setInt(1, user.getUserid()); // Giả sử bạn có phương thức getUserid để lấy ID người dùng
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                banned = rs.getBoolean("banned");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//
//        return banned;
//    }
//
//    public ArrayList<Movie> getMovies() {
//        ArrayList<Movie> movies = new ArrayList<>();
//        String sql = "SELECT M.movieid, M.namemovie, M.director, M.release_year, MG.genreid , Mg.genrename "
//                + " FROM MOVIE M INNER JOIN GENRE MG ON M.GENREID = MG.GENREID ";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//
//            while (rs.next()) {
//                Movie m = new Movie();
//                m.setMovieid(rs.getInt("movieid"));
//                m.setNamemovie(rs.getString("namemovie"));
//                m.setDirector(rs.getString("director"));
//                m.setRelease_year(rs.getInt("release_year"));
//
//                MovieGenre mg = new MovieGenre();
//                mg.setGenreid(rs.getInt("genreid"));
//                mg.setGenrename(rs.getString("genrename"));
//                m.setGenreid(mg);
//                movies.add(m);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return movies;
//    }
//
//    public ArrayList<MovieGenre> getGenres() {
//        ArrayList<MovieGenre> genres = new ArrayList<>();
//        String sql = "SELECT GENREID,GENRENAME FROM GENRE";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//            while (rs.next()) {
//                MovieGenre mg = new MovieGenre();
//                mg.setGenreid(rs.getInt("genreid"));
//                mg.setGenrename(rs.getString("genrename"));
//                genres.add(mg);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return genres;
//    }
//
//    public void insertMovie(Movie movie) {
//        String sql = "INSERT INTO MOVIE (MOVIEID, NAMEMOVIE, DIRECTOR, RELEASE_YEAR, GENREID) VALUES (?, ?, ?, ?, ?)";
//        try (PreparedStatement st = connect.prepareStatement(sql)) {
//            st.setInt(1, movie.getMovieid());
//            st.setString(2, movie.getNamemovie());
//            st.setString(3, movie.getDirector());
//            st.setInt(4, movie.getRelease_year());
//            st.setInt(5, movie.getGenreid().getGenreid());
//
//            st.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    public void updateMovie(Movie movie) {
//        String sql = "UPDATE Movie SET namemovie = ?, director = ?, release_year = ?,genreid = ? WHERE movieid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//
//            ps.setString(1, movie.getNamemovie());
//            ps.setString(2, movie.getDirector()); // Correct date conversion
//            ps.setInt(3, movie.getRelease_year());
//            ps.setInt(4, movie.getGenreid().getGenreid());
//            ps.setInt(5, movie.getMovieid());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void hideMovie(int id) {
//        String sql = "UPDATE Movie SET isHidden = TRUE WHERE movieid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteMovie(int id) {
//        String sql = "DELETE FROM Movie WHERE movieid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //list movie
//    public ArrayList<Movie> getMovieByGenre(int genreid) {
//        ArrayList<Movie> movie = new ArrayList<>();
//        try {
//            String sql = "SELECT M.movieid, M.namemovie, M.director, M.release_year, MG.genreid , Mg.genrename "
//                    + " FROM MOVIE M INNER JOIN GENRE MG ON M.GENREID = MG.GENREID "
//                    + "where mg.genreid = ?";
//            PreparedStatement ps = connect.prepareStatement(sql);
//            ps.setInt(1, genreid);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Movie m = new Movie();
//                m.setMovieid(rs.getInt("movieid"));
//                m.setNamemovie(rs.getString("namemovie"));
//                m.setDirector(rs.getString("director"));
//                m.setRelease_year(rs.getInt("release_year"));
//
//                MovieGenre mg = new MovieGenre();
//                mg.setGenreid(rs.getInt("genreid"));
//                mg.setGenrename(rs.getString("genrename"));
//                m.setGenreid(mg);
//                movie.add(m);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return movie;
//    }
//
//    public Movie getMoviesbyId(int id) {
//        Movie movie = null;
//        String sql = "SELECT M.movieid, M.namemovie, M.director, M.release_year, MG.genreid , Mg.genrename "
//                + " FROM MOVIE M "
//                + " INNER JOIN GENRE MG ON M.GENREID = MG.GENREID "
//                + " where M.movieid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setInt(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                int movieid = rs.getInt("movieid");
//                String namemovie = rs.getString("namemovie");
//                String director = rs.getString("director");
//                int release_year = rs.getInt("release_year");
//
//                MovieGenre genre = new MovieGenre();
//                genre.setGenreid(rs.getInt("genreid"));
//                genre.setGenrename(rs.getString("genrename"));
//
//                movie = new Movie(movieid, namemovie, director, release_year, genre);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return movie;
//    }
//
//    //edit movie
//    public MovieGenre getMovieGenreById(int genreid) {
//        MovieGenre mg = null;
//        String sql = "SELECT genreid, genrename "
//                + "FROM Genre WHERE genreid = ? ";
//        try (PreparedStatement statement = connect.prepareStatement(sql)) {
//            statement.setInt(1, genreid);
//            try (ResultSet rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    mg = new MovieGenre();
//                    mg.setGenreid(rs.getInt("genreid"));
//                    mg.setGenrename(rs.getString("genrename"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return mg;
//    }
//
//    public ArrayList<Users> getUsers() {
//        ArrayList<Users> users = new ArrayList<>();
//        String sql = "SELECT * "
//                + "FROM Users U INNER JOIN Role R ON U.roleid = R.roleid "
//                + "WHERE banned = 0 ";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//
//            while (rs.next()) {
//                Users u = new Users();
//                u.setUserid(rs.getInt("userid"));
//                u.setUsername(rs.getString("username"));
//                u.setUserpassword(rs.getString("userpassword"));
//
//                Role r = new Role();
//                r.setRoleid(rs.getInt("roleid"));
//                r.setNamerole(rs.getString("namerole"));
//                u.setRoleid(r);
//                users.add(u);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return users;
//    }
//
//    public ArrayList<Users> getUserUnBan() {
//        ArrayList<Users> users = new ArrayList<>();
//        String sql = "SELECT * "
//                + "FROM Users U INNER JOIN Role R ON U.roleid = R.roleid "
//                + "WHERE banned = 1 ";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//
//            while (rs.next()) {
//                Users u = new Users();
//                u.setUserid(rs.getInt("userid"));
//                u.setUsername(rs.getString("username"));
//                u.setUserpassword(rs.getString("userpassword"));
//
//                Role r = new Role();
//                r.setRoleid(rs.getInt("roleid"));
//                r.setNamerole(rs.getString("namerole"));
//                u.setRoleid(r);
//                users.add(u);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return users;
//    }
//
//    public ArrayList<Users> getUsersByRole(int roleid) {
//        ArrayList<Users> us = new ArrayList<>();
//        String sql = "SELECT * FROM Users U INNER JOIN Role R ON U.roleid = R.roleid WHERE R.roleid = ?";
//
//        try (PreparedStatement statement = connect.prepareStatement(sql)) {
//            statement.setInt(1, roleid);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    Users u = new Users();
//                    u.setUserid(rs.getInt("userid"));
//                    u.setUsername(rs.getString("username"));
//                    u.setUserpassword(rs.getString("userpassword"));
//
//                    Role r = new Role();
//                    r.setRoleid(rs.getInt("roleid"));
//                    r.setNamerole(rs.getString("namerole"));
//                    u.setRoleid(r);
//
//                    us.add(u);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return us;
//    }
//
//    //edit user
//
//    //edit user
//    public Role getRoleById(int roleid) {
//        Role rl = null;
//        String sql = "SELECT roleid, namerole "
//                + "FROM Role "
//                + "WHERE roleid = ? ";
//        try (PreparedStatement statement = connect.prepareStatement(sql)) {
//            statement.setInt(1, roleid);
//            try (ResultSet rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    rl = new Role();
//                    rl.setRoleid(rs.getInt("roleid"));
//                    rl.setNamerole(rs.getString("namerole"));
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return rl;
//    }
//
//    public ArrayList<Role> getRoles() {
//        ArrayList<Role> roles = new ArrayList<>();
//        String sql = "SELECT * FROM Role";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//
//            while (rs.next()) {
//                Role r = new Role();
//                r.setRoleid(rs.getInt("roleid"));
//                r.setNamerole(rs.getString("namerole"));
//                roles.add(r);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return roles;
//    }
//
//    public Users getUserByName(String name) throws Exception {
//        String query = "SELECT * FROM Users WHERE username=?";
//        PreparedStatement ps = connect.prepareStatement(query);
//        ps.setString(1, name);
//        ResultSet rs = ps.executeQuery();
//
//        if (rs.next()) {
//            Users user = new Users();
//            user.setUserid(rs.getInt("userid"));
//            user.setUsername(rs.getString("username"));
//            user.setUserpassword(rs.getString("userpassword"));
//
//            // Get the role of the user
//            int roleId = rs.getInt("roleid");
//            Role role = getRoleById(roleId);
//            user.setRoleid(role);
//
//            return user;
//        }
//        return null;
//    }
//
//    public void deleteUser(int userid) {
//        String sql = "DELETE FROM Users WHERE userid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setInt(1, userid);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updateUser(Users user) {
//        String sql = "UPDATE Users SET username = ?, userpassword = ?,roleid = ? WHERE userid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//
//            ps.setString(1, user.getUsername());
//            ps.setString(2, user.getUserpassword()); // Correct date conversion
//
//            ps.setInt(3, user.getRoleid().getRoleid());
//            ps.setInt(4, user.getUserid());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void insertUser(Users user) {
//        String sqlInsertUser = "INSERT INTO Users (userid, username, userpassword, roleid) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement psUser = connect.prepareStatement(sqlInsertUser)) {
//            psUser.setInt(1, user.getUserid());
//            psUser.setString(2, user.getUsername());
//            psUser.setString(3, user.getUserpassword());
//            psUser.setInt(4, user.getRoleid().getRoleid());
//            psUser.executeUpdate();
//
//            String sqlGetUserId = "SELECT userid FROM Users WHERE username = ? AND userpassword = ?";
//            PreparedStatement psGetUserId = connect.prepareStatement(sqlGetUserId);
//            psGetUserId.setString(1, user.getUsername());
//            psGetUserId.setString(2, user.getUserpassword());
//            ResultSet rs = psGetUserId.executeQuery();
//
//            if (rs.next()) {
//                int userId = rs.getInt("userid");
//
//                String sqlInsertProfile = "INSERT INTO Profile (profileid, userid) VALUES (?, ?)";
//                try (PreparedStatement psProfile = connect.prepareStatement(sqlInsertProfile)) {
//                    psProfile.setInt(1, getNextProfileId());
//                    psProfile.setInt(2, userId);
//                    psProfile.executeUpdate();
//                }
//            } else {
//                throw new SQLException("Failed to retrieve userid after insert.");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    public int getNextUserId() {
//        String sql = "SELECT MAX(userid) FROM Users";
//        try (PreparedStatement ps = connect.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) {
//                return rs.getInt(1) + 1;
//            }
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//        return 1;
//    }
//
//
//    
//
//    public ArrayList<UserViewMovie> getRate() {
//        ArrayList<UserViewMovie> rate = new ArrayList<>();
//        String sql = "SELECT U.userid, M.movieid, V.rating, V.comment "
//                + "FROM UserViewMovie V INNER JOIN Users U ON V.userid = U.userid "
//                + "INNER JOIN Movie M ON V.movieid = M.movieid ";
//        try (PreparedStatement statement = connect.prepareStatement(sql); ResultSet rs = statement.executeQuery()) {
//            while (rs.next()) {
//                UserViewMovie mg = new UserViewMovie();
//                mg.setRating(rs.getInt("rating"));
//                mg.setComment(rs.getString("comment"));
//
//                Users u = new Users();
//                u.setUserid(rs.getInt("userid"));
//                mg.setUserid(u);
//
//                Movie m = new Movie();
//                m.setMovieid(rs.getInt("movieid"));
//                mg.setMovieid(m);
//
//                rate.add(mg);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return rate;
//    }
//
//    public ArrayList<UserViewMovie> getRatebymovieId(int movieId) {
//        ArrayList<UserViewMovie> ratemv = new ArrayList<>();
//        String sql = "SELECT * FROM UserViewMovie WHERE movieId = ?";
//
//        try (PreparedStatement statement = connect.prepareStatement(sql)) {
//            statement.setInt(1, movieId);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    UserViewMovie mg = new UserViewMovie();
//                    mg.setRating(rs.getInt("rating"));
//                    mg.setComment(rs.getString("comment"));
//
//                    Movie m = new Movie();
//                    m.setMovieid(rs.getInt("movieId"));
//                    mg.setMovieid(m);
//
//                    ratemv.add(mg);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return ratemv;
//    }
//
//    public ArrayList<UserViewMovie> getRatebyuserid(int userid) {
//        ArrayList<UserViewMovie> ratemv = new ArrayList<>();
//        String sql = "SELECT * FROM UserViewMovie WHERE userid = ?";
//
//        try (PreparedStatement statement = connect.prepareStatement(sql)) {
//            statement.setInt(1, userid);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    UserViewMovie mg = new UserViewMovie();
//                    mg.setRating(rs.getInt("rating"));
//                    mg.setComment(rs.getString("comment"));
//
//                    Movie m = new Movie();
//                    m.setMovieid(rs.getInt("movieId"));
//                    mg.setMovieid(m);
//
//                    ratemv.add(mg);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return ratemv;
//    }
//
//    //edit rate
//    public UserViewMovie getRatebymvid(int id) {
//        UserViewMovie ratemv = null;
//        String sql = "SELECT U.userid, M.movieid, UVM.rating, UVM.comment "
//                + "FROM UserViewMovie UVM "
//                + "INNER JOIN Users U on UVM.userid = U.userid "
//                + "INNER JOIN Movie M on UVM.movieid = M.movieid "
//                + "WHERE M.movieid = ?";
//
//        try (PreparedStatement statement = connect.prepareStatement(sql)) {
//            statement.setInt(1, id);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    Users u = new Users();
//                    u.setUserid(rs.getInt("userid"));
//                    Movie m = new Movie();
//                    m.setMovieid(rs.getInt("movieid"));
//
//                    int rating = rs.getInt("rating");
//                    String comment = rs.getString("comment");
//                    ratemv = new UserViewMovie(u, m, rating, comment);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exceptions
//        }
//        return ratemv;
//    }
//
//    public void insertRating(int userid, int movieid, int rating, String comment) throws Exception {
//        String sql = "INSERT INTO UserViewMovie (userid, movieid, rating, comment) VALUES (?, ?, ?, ?)";
//
//        try (
//                PreparedStatement stmt = connect.prepareStatement(sql)) {
//            // Set parameters
//            stmt.setInt(1, userid);
//            stmt.setInt(2, movieid);
//            stmt.setInt(3, rating);
//            stmt.setString(4, comment);
//
//            // Execute insert
//            stmt.executeUpdate();
//        }
//    }
//
//    //add rate
//    public void addRating(UserViewMovie uvm) {
//        String query = "INSERT INTO UserViewMovie (userid, movieid, rating, comment) VALUES (?, ?, ?, ?)";
//
//        try (PreparedStatement st = connect.prepareStatement(query)) {
//            st.setInt(1, uvm.getUserid().getUserid());
//            st.setInt(2, uvm.getMovieid().getMovieid());
//            st.setInt(3, uvm.getRating());
//            st.setString(4, uvm.getComment());
//
//            st.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    //edit rate
//    public void EditRateMovie(UserViewMovie uvm) {
//        String sql = "UPDATE UserViewMovie SET userid = ?, rating = ?, comment = ? WHERE movieid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//
//            ps.setInt(1, uvm.getUserid().getUserid());
//            ps.setInt(2, uvm.getRating());
//            ps.setString(3, uvm.getComment());
//            ps.setInt(4, uvm.getMovieid().getMovieid());
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //delete rate
//    public void deleteRating(int movieid) {
//        String sql = "DELETE FROM UserViewMovie WHERE movieid = ?";
//        try (PreparedStatement ps = connect.prepareStatement(sql)) {
//            ps.setInt(1, movieid);
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//
//        DAO dao = new DAO();
////        MovieGenre type = new MovieGenre(9, "");
////        Movie movie = new Movie(20, "La La Land", "Damien Chazelle", 2016, type);
////        Role roleid = new Role(2, "");
////        Users user = new Users(99, "nharang", "123", roleid);
////        System.out.println(dao.getMovies());
////
////        System.out.println(dao.getUserByName("viet"));
////        dao.deleteUser(3);
////        dao.registerUser(user);
////2	20	La La Land	Damien Chazelle	2016	Musical	2
////3	The Dark Knight	Christopher Nolan	2008	1
////4	Pulp Fiction	Quentin Tarantino	1994	2
////5	Interstellar	Christopher Nolan	2014	1
////6	Fight Club	David Fincher	1999	2
////7	The Shawshank Redemption	Frank Darabont	1994	2
////8	The Matrix	Lana Wachowski, Lilly Wachowski	1999	1
////9	Forrest Gump	Robert Zemeckis	1994	2
////10	Gladiator	Ridley Scott	2000	3
//        // Insert into database
////        dao.updateMovie(movie);
//        //dao.insertMovie(movie);
////        dao.registerUser(user);
////        System.out.println(dao.getRoleById(1).getNamerole());
////        System.out.println(dao.getRate());
////        int movieId = 1; // ID của phim Inception từ hình của bạn
////        System.out.println(dao.getRatebymovieId(movieId));
////        System.out.println(dao.getUsersByRole(1));
////        String com = "Good";
////        dao.addRating(1, 2, 5, com);
//        //dao.addRating(uvm);
//        //dao.deleteRating(21);
//        //UserViewMovie uvm = new UserViewMovie(userid, movieid, 0, comment)
//        //dao.EditRateMovie(uvm);
//        //System.out.println(dao.getRatebymvid(1));
////        String userid = "4";
////        String movieid = "1";
////        String comment = "comment";
////        dao.insertRating(userid, movieid, 1, comment);
////        Profile profile = new Profile(12, null , 0, null, null, User);
////        dao.insertProfile(profile);
////        Role role = new Role(2, "");
////        Users user = new Users(13, "testtt", "123", role);
////        dao.insertUser(user);
//        System.out.println(dao.getProfileById(1));
//        dao.unbanUser(15);
//        System.out.println("------");
//    }
    public static void main(String[] args) throws Exception {
        DAO dao = new DAO();
//        Users admin = new Users(0, "Admin2", "1234", 1, "Hoangvanviet2", today, today, 0, 0, today, 0);
//        dao.Register(admin, 0);
        System.out.println(dao.getUsersBySearch("viet"));
       //Users user = new Users(3, "Admin", "admin", 1, "Hoangvanviet123", today, today, 0, 0, today, 0);
//        dao.updateUser(user);
//        System.out.println(dao.getUserByName("Admin2"));
        //dao.updateUser(user);
        
        

    }
}
