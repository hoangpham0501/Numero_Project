package bubble.numero;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Random;

public class Generate {

    static Random rand ;
    static JSONObject obj;

    static int[] intArray = new int[109];
	
	public Generate() throws IOException, ParseException {
		rand = new Random();
		JSONParser parser = new JSONParser();
		BufferedReader reader = new BufferedReader(new InputStreamReader(GameActivity.inputStream));

		String data = "";
		String x = "";

		// read All DATA
		while ((x = reader.readLine()) != null)
			data+=x ;

		obj = (JSONObject) parser.parse(data);
	}
	
	public static Bubble [][] load (String level)
	{
        Bubble [][] array = null ;
        // get Level Array
        JSONArray arr = (JSONArray)obj.get(level);
        array = new Bubble [GameScene.arrayLenght][GameScene.numOfBubbPerRow-1];

        Iterator<Long> it = arr.iterator();

        // fill the Array with the bubbles
        int maxi = arr.size()/(GameScene.numOfBubbPerRow-1);
        maxi = maxi > GameScene.arrayLenght? GameScene.arrayLenght: maxi;
//        System.out.println("arrayLenght = " + GameScene.arrayLenght);
//        System.out.println("max i = " + arr.size()/(GameScene.numOfBubbPerRow-1));

        int maxNum = (maxi - 2) * array[0].length;
//        System.out.println("maxNum = " + maxNum);

        for (int i = 0; i < maxNum; i++) {
            if (MainMenuActivity.gameMode.equals("Classical")) intArray[i] = i+1;
            else if (MainMenuActivity.gameMode.equals("Challenge")) {
                if (i == 0) intArray[i] = 1;
                else intArray[i] = intArray[i-1] + i;
            }
        }

        int[] randomIntArray = new int[maxNum];
        for (int i = 0; i < maxNum; i++) {
            randomIntArray[i] = intArray[i];
        }

        Random generator = new Random();
        int x, y, tmp;
        for (int i = 0; i < maxNum; i++) {
            x = generator.nextInt(maxNum);
            y = generator.nextInt(maxNum);

            tmp = randomIntArray[x];
            randomIntArray[x] = randomIntArray[y];
            randomIntArray[y] = tmp;
        }

        int nextNum = 0;
        for (int i = 0; i < maxi - 2; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = new Bubble(j * GameScene.bubbleSize, i * GameScene.bubbleSize, it.next().intValue(), randomIntArray[nextNum]);
                nextNum++;
            }
        }

        return array ;
	}
	
}
