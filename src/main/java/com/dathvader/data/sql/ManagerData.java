package com.dathvader.data.sql;

/*
public class ManagerData implements Runnable{

    private final Connection connection;
    private AverageCalc averageCalc = new AverageCalc("replicadata");

    private BlockingQueue blockingQueue = new LinkedBlockingQueue<String>();

    private String url = "data/_cache.db";

    public ManagerData() {

        File file = null;
        if(!(file = new File(url)).exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connection = newConnection(url);
        createDatabase();
        executeUpdate(createTable);
        new Thread(this).start();
    }


    public void getPlayerInformations(String id, Callback<String> callback) {
        long begin = System.currentTimeMillis();

        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT motdepasse FROM auth_clients WHERE pseudonyme=?");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            callback.completeFuture((resultSet.next()) ? resultSet.getString("motdepasse") : null);
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        this.averageCalc.addValue(System.currentTimeMillis() - begin);
    }

    public void executeUpdate(String sql){
        blockingQueue.add(sql);
    }

    public void createDatabase() {
        try {
            Connection connection = getConnection();
            connection.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection newConnection(String url){
        try {
            return DriverManager.getConnection("jdbc:sqlite:"+ url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getPosition(){
        return 1;
    }


    @Override
    public void run() {
        try {
            Connection connection = getConnection();
            while (true){
                    if(blockingQueue.isEmpty()) continue;
                String sql = (String) blockingQueue.take();
                if(sql.startsWith("truncate"))
                    sql = "delete from auth_clients";
                sql = (String) sql.replace("now()", String.valueOf(System.currentTimeMillis()));
                try{
                    connection.prepareStatement(sql).execute();
                } catch (SQLException e) {
                    System.out.println("\nError sql: "+e.getMessage()+"" +
                            "\nRequest: "+sql);
                    //e.printStackTrace();
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
*/