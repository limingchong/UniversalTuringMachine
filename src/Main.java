package utm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // default parameters
        String DeltaFunction = "Decides 0^n1^n";
        String rowRules = "q0,*,qa,*,RIGHT<>q0,1,qr,1,RIGHT<>q0,0,q1,X,RIGHT<>q1,0,q1,0,RIGHT<>q1,1,q1,1,RIGHT<>q1,X,q2,X,LEFT<>q1,*,q2,*,LEFT<>q2,X,qr,X,LEFT<>q2,0,qr,0,LEFT<>q2,1,q3,X,LEFT<>q3,1,q3,1,LEFT<>q3,0,q3,0,LEFT<>q3,X,q4,X,RIGHT<>q4,0,q1,X,RIGHT<>q4,X,qa,X,RIGHT<>q4,1,qr,1,RIGHT<>q0,X,qr,X,RIGHT<>q2,*,qr,*,RIGHT<>q3,*,qr,*,RIGHT<>q4,*,qr,*,RIGHT";
        String initialState = "q0";
        String acceptState = "qa";
        String rejectState = "qr";

        // optional parameters
        String input = "01";
        boolean animation = true;
        switch (args.length){
            case 1:
                input = args[0];
                break;
            case 2:
                input = args[0];
                animation = args[1].equals("true") ? true : false;
                break;
            default:
                break;
        }

        UniversalTuringMachine UTM = new UniversalTuringMachine();
        UTM.setTitle(UTM.getTitle() + " - " + DeltaFunction);

        // set rules
        String rules[] = rowRules.split("<>");
        TuringMachine TM = new TuringMachine(rules.length,initialState,acceptState,rejectState);
        for (String rule : rules){
            String[] splitRule = rule.split(",");
            MoveClassical move = splitRule[4].equals("RIGHT") ? MoveClassical.RIGHT : MoveClassical.LEFT;
            TM.addRule(splitRule[0],splitRule[1].toCharArray()[0],splitRule[2],splitRule[3].toCharArray()[0],move);
        }
        UTM.loadTuringMachine(TM);

        UTM.loadInput(input);
        UTM.display();

        // start
        String currentState = null;
        String currentCell = null;
        while (!TM.getHead().getCurrentState().equals("qa") & !TM.getHead().getCurrentState().equals("qr"))
        {
            currentState = TM.getHead().getCurrentState();
            currentCell = String.valueOf(TM.getTape().get(TM.getHead().getCurrentCell()));

            // record
            System.out.println(currentState + "," + currentCell);

            // check rules
            for (String rule : rules){
                String[] splitRule = rule.split(",");
                if (splitRule[0].equals(currentState) && splitRule[1].equals(currentCell)){
                    UTM.updateHeadState(splitRule[2]);
                    UTM.writeOnCurrentCell(splitRule[3].toCharArray()[0]);
                    MoveClassical move = splitRule[4].equals("RIGHT") ? MoveClassical.RIGHT : MoveClassical.LEFT;
                    UTM.moveHead(move,animation);
                    break;
                }
            }
        }

        // end
        UTM.displayHaltState(TM.getHead().getCurrentState().equals("qa") ? HaltState.ACCEPTED : HaltState.REJECTED);

    }
}
