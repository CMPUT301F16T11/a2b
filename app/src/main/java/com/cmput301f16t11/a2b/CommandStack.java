package com.cmput301f16t11.a2b;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by tothd on 11/26/2016.
 */

public class CommandStack {
    private static File directory;
    private static ArrayList<UserRequest> AcceptedCommands = new ArrayList<>();
    private static ArrayList<UserRequest> AddCommands = new ArrayList<>();

    public static String ACCEPTFILE = "OfflineAcceptance.sav";
    public static String ADDFILE = "OfflineAdd.sav";

    public static void setDirectory(File file){
        directory = file;
    }

    public static void setAcceptedCommands(ArrayList<UserRequest> commands){
        AcceptedCommands = commands;
    }
    public static void setAddCommands(ArrayList<UserRequest> commands){
        AddCommands = commands;
    }
    public static void clearCommands(){
        AcceptedCommands = new ArrayList<>();
        AddCommands = new ArrayList<>();
    }
    public static UserRequest checkAccepted(int i){
        return AcceptedCommands.get(i);
    }
    public static UserRequest checkAdd(int i){
        return AddCommands.get(i);
    }

    public static boolean isValidCommand(UserRequest request){
        ElasticsearchRequestController.GetOpenRequestById getId = new ElasticsearchRequestController.GetOpenRequestById();
        getId.execute(request.getId());
        try{
            if(!(getId.get()==null)){
                return true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public static void addAddCommand(UserRequest command, Context con){
        if (AddCommands==null) {
            AddCommands = new ArrayList<>();
        }
        AddCommands.add(command);
        FileController.saveInFile(AddCommands,CommandStack.ADDFILE, con);
    }

    public static void addAcceptedCommand(UserRequest command, Context con){
        if (AcceptedCommands==null) {
            AcceptedCommands = new ArrayList<>();
        }
        AcceptedCommands.add(command);
        FileController.saveInFile(AcceptedCommands,CommandStack.ACCEPTFILE, con);
    }

    public static boolean workRequired(){
        try {
            return ((AddCommands.size() > 0) || (AcceptedCommands.size() > 0));
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static void handleStack(Context context){
        if (!(AcceptedCommands==null)) {
            for(UserRequest request: AcceptedCommands){
                if(isValidCommand(request)){
                    RequestController.addAcceptanceOffline(request);
                }
            }
        }
        if (!(AddCommands==null)) {
            ArrayList<UserRequest> filledOutRequests = new ArrayList<>();
            for (UserRequest request : AddCommands) {
                filledOutRequests.add(RequestController.convertOfflineRequestToOnlineRequest(request, context));
            }
            RequestController.addBatchOpenRequests(filledOutRequests, context);
        }

        clearCommands();
        //delete save file
        File accSavFile = new File(directory,CommandStack.ACCEPTFILE);
        File addSavFile = new File(directory,CommandStack.ADDFILE);
        accSavFile.delete();
        addSavFile.delete();
    }
}
