public class Valid{

    //no need to instantiation
    public static boolean zip(String input){
        //min
        //max

        //"Seive"
        if(input.length() != 5){
            return false;
        }

        //letters? Special Characters?
       
        /**Blacklist
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);

            if(Character.isAlphabetic(c)){
                return false;
            }
        } */

        //Whitelist
        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);

            if(!Character.isDigit(c)){
                return false;
            }
        }

        return true;
    }

    public static boolean name(String input){
        //LEN
        if(input.length() < 2 || input .length() > 128){
            return false;
        }

        //WhiteList
       /**  for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);

            if(Character.isDigit(c)){
                return false;
            }
        }
        */

        //REGEX - pattern matching engine
        //Special formatted string
        // llllll llllll
        //[A-Za-z'-]{1,32} [A-Za-z'-]{1,32}


        return false;

    }
}