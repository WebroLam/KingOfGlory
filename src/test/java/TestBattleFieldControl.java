import com.jerry.mapObjects.heroes.Hero;
import org.junit.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

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
	public void TestMapPrint() {

	}

	@Test
	public void TestHeroMove() {

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

}
