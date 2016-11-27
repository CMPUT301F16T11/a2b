package com.cmput301f16t11.a2b;

import java.util.ArrayList;

/**
 * Created by tothd on 11/26/2016.
 */

public class CommandStack {

    private static ArrayList<Command> Commands = new ArrayList<>();

    public static void addCommand(Command command   ){
        Commands.add(command);
    }
    public static boolean handleCommand(Command command){


        if(command.getStatus()){
            ElasticsearchRequestController.AddOpenRequestTask addTask = new ElasticsearchRequestController.AddOpenRequestTask();
            addTask.execute(command.getRequest());
            try{
                return addTask.get();
            }catch (Exception e){

                e.printStackTrace();
            }

        }
        else {
            ElasticsearchRequestController.AddDriverAcceptanceToRequest addAcceptance = new ElasticsearchRequestController.AddDriverAcceptanceToRequest();
            addAcceptance.execute(command.getRequest().getId(),UserController.getUser().getId());
            try{
                return  addAcceptance.get();
            } catch (Exception e){
                e.printStackTrace();
            }

        }
    return false;
    }
    public void handleStack(){
        for(Command command : Commands){
            if(!command.getStatus()){
                if(command.isValidCommand()){
                    handleCommand(command);
                }
            }else{
                handleCommand(command);
            }
        }
    }
}
