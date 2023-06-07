package test;

import model.Tank;
import model.Utils;
import org.junit.Assert;
import org.junit.Test;

public class MyTest {
    @Test
    public void testTank() {
        Tank tank = new Tank(1, null, 0, 0);
        Assert.assertFalse(tank.canFire());
        Assert.assertFalse(Utils.isCollide(0, 0, 0, 0, 0, 0));
    }
}
