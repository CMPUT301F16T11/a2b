package com.cmput301f16t11.a2b;

public class Command{
        private UserRequest request;
        private boolean status;

        public Command(UserRequest request, Boolean status){
                this.request = request;
                this.status = status;
        }
        public UserRequest getRequest(){
                return this.request;
        }
        public boolean getStatus(){
                return this.status;
        }

        public boolean isValidCommand(){
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
}
