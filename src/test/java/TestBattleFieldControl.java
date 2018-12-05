import com.jerry.glory.GUI;
import com.jerry.mapObjects.Location;
import com.jerry.mapObjects.MapObject;
import com.jerry.mapObjects.heroes.Hero;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static com.jerry.jsonHandle.readJSONStringFromFile;
import com.jerry.mapObjects.*;
import com.jerry.mapObjects.heroes.*;

import java.util.HashMap;

public class TestBattleFieldControl {


    Hero hero1,hero2;
    private static final Logger logger = LogManager.getLogger(TestBattleFieldControl.class);
    public static final String ResourceFilePath = "src/main/resources";

    @BeforeEach
    public void init() {
        hero1 = new Hero();
	}

	@Test
	public void testGetAttacked() {
    	Hero hero1 = new Hero();
    	int original = 100;
    	int damage = 10;
		Assertions.assertEquals(original - damage, 90);
	}
	@Test
    @DisplayName("Testing hero get damaged")
	public void testGetDamaged() {
    	init();
        int damageTotake = (int)(Math.random() * 100);
    	int initialHealth = hero1.getCurrentHealth();
    	hero1.beAttacked(damageTotake);
        Assertions.assertEquals(initialHealth-damageTotake,hero1.getCurrentHealth());
	}
	@Test
	public void TestHeroDieAndReSpawn() {
        init();
        int fullHealth = hero1.getCurrentHealth();
        hero1.beAttacked(hero1.getCurrentHealth());
        Assertions.assertEquals(0,hero1.getCurrentHealth());
        hero1.ReSpawn();
        Assertions.assertEquals(fullHealth,hero1.getCurrentHealth());
	}
	@Test
	@DisplayName("Test Warrior")
	public void testWarrior() {
		try{
			JSONObject jsonObject = new JSONObject(readJSONStringFromFile("Heroes.json"));
			JSONArray heroJSON = jsonObject.getJSONArray("Heroes");
			GUI game = new GUI();

			autoWarrior warrior = new autoWarrior(heroJSON.getJSONObject(0),game);
			game.mapObjectLocation.put(warrior,new Location(1,1));
			game.heroes.insertElementAt(warrior,0);
			Assertions.assertFalse(game.mapObjectLocation.get(warrior).distanceTo(new Location(1,1)) > 0);

		} catch (JSONException e) {
			System.out.println(e.toString());
		}
	}

}
