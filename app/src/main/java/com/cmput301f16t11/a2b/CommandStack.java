package com.cmput301f16t11.a2b;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tothd on 11/26/2016.
 */
public class CommandStack {
    private static File directory;
    private static ArrayList<UserRequest> AcceptedCommands = new ArrayList<>();
    private static ArrayList<UserRequest> AddCommands = new ArrayList<>();
    private static boolean EditProfile = false;

    /**
     * The constant ACCEPTFILE.
     */
    public static String ACCEPTFILE = "OfflineAcceptance.sav";
    /**
     * The constant ADDFILE.
     */
    public static String ADDFILE = "OfflineAdd.sav";

    /**
     * Set directory.
     *
     * @param file the file
     */
    public static void setDirectory(File file){
        directory = file;
    }

    /**
     * Method to set/clear need for updating user in DB.
     *
     * @param bool Boolean to edit
     */
    public static void setEditProfile(Boolean bool) {
        EditProfile = bool;
    }

    /**
     * Set accepted commands.
     *
     * @param commands the commands
     */
    public static void setAcceptedCommands(ArrayList<UserRequest> commands){
        AcceptedCommands = commands;
    }

    /**
     * Set add commands.
     *
     * @param commands the commands
     */
    public static void setAddCommands(ArrayList<UserRequest> commands){
        AddCommands = commands;
    }

    /**
     * Get add commands array list.
     *
     * @return the array list
     */
    public static ArrayList<UserRequest> getAddCommands(){return AddCommands;}

    /**
     * Get accepted commands array list.
     *
     * @return the array list
     */
    public static ArrayList<UserRequest> getAcceptedCommands(){return AcceptedCommands;}

    /**
     * Method to tell command stack if the profile needs to be edited
     *
     * @return EditProfile boolean
     */
    public static boolean getEditProfile() {
        return EditProfile;
    }

    /**
     * Clear commands.
     */
    public static void clearCommands(){
        AcceptedCommands = new ArrayList<>();
        AddCommands = new ArrayList<>();
    }

    /**
     * Check accepted user request.
     *
     * @param i the
     * @return the user request
     */
    public static UserRequest checkAccepted(int i){
        return AcceptedCommands.get(i);
    }

    /**
     * Check add user request.
     *
     * @param i the
     * @return the user request
     */
    public static UserRequest checkAdd(int i){
        return AddCommands.get(i);
    }

    /**
     * Is valid command boolean.
     *
     * @param request the request
     * @return the boolean
     */
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


    /**
     * Add add command.
     *
     * @param command the command
     * @param con     the con
     */
    public static void addAddCommand(UserRequest command, Context con){
        if (AddCommands==null) {
            AddCommands = new ArrayList<>();
        }
        if(!AddCommands.contains(command)) {
            AddCommands.add(command);
            FileController.saveInFile(AddCommands, CommandStack.ADDFILE, con);
        }
    }

    /**
     * Add accepted command.
     *
     * @param command the command
     * @param con     the con
     */
    public static void addAcceptedCommand(UserRequest command, Context con){
        if (AcceptedCommands==null) {
            AcceptedCommands = new ArrayList<>();
        }
        if(!AcceptedCommands.contains(command)) {
            AcceptedCommands.add(command);
            FileController.saveInFile(AcceptedCommands, CommandStack.ACCEPTFILE, con);
        }
    }

    /**
     * Work required boolean.
     *
     * @return the boolean
     */
    public static boolean workRequired() {
        if (!(AddCommands==null) & !(AcceptedCommands==null)) {
            return (AddCommands.size()>0) || (AcceptedCommands.size()>0) || EditProfile;
        } else if (!(AddCommands==null)) {
            return AddCommands.size()>0 || EditProfile;
        } else if (!(AcceptedCommands==null)) {
            return AcceptedCommands.size()>0 || EditProfile;
        }
        return EditProfile;
    }

    /**
     * Handle stack.
     *
     * @param context the context
     */
    public static void handleStack(Context context){
        if (!(AcceptedCommands==null)) {
            for(UserRequest request: AcceptedCommands){
                if(isValidCommand(request)){
                    RequestController.addAcceptanceOffline(request);
                    try {
                        DriverNotificationService.serviceHandler(request, (Activity) context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("driverNotServ", e.toString());
                    }
                }
            }
        }
        if (!(AddCommands==null)) {
            ArrayList<UserRequest> filledOutRequests = new ArrayList<>();
            for (UserRequest request : AddCommands) {
                UserRequest temp = RequestController.convertOfflineRequestToOnlineRequest(request, context);
                filledOutRequests.add(temp);
            }
            RequestController.addBatchOpenRequests(filledOutRequests, context);
            for (UserRequest request : filledOutRequests) {
                RiderNotificationService.addRequestToBeNotified(request);
            }
        }
        if (EditProfile) {
            UserController.updateUserInDbOffline();
            EditProfile = false;
        }

        clearCommands();
        //delete save file
        File accSavFile = new File(directory,CommandStack.ACCEPTFILE);
        File addSavFile = new File(directory,CommandStack.ADDFILE);
        accSavFile.delete();
        addSavFile.delete();
    }
}
