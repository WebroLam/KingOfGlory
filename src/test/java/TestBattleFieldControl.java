import com.jerry.mapObjects.heroes.Hero;
import org.junit.*;
import org.junit.jupiter.api.BeforeAll;

public class TestBattleFieldControl {


    Hero hero1,hero2;

	@BeforeAll
    public void init() {

	}



	@Test
	public void testGetDamaged() {
	    hero1 = new Hero();
        int beforeHealth = hero1.getCurrentHealth();
        hero1.beAttacked(10);
        Assert.assertEquals(beforeHealth - 10,hero1.getCurrentHealth());
	}

	@Test
	public void TestMapPrint() {

	}

	@Test
	public void TestHeroMove() {

	}

	@Test
	public void TestHeroDieAndReSpawn() {

	}

}
