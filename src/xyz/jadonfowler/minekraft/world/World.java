package xyz.jadonfowler.minekraft.world;

import org.lwjgl.input.*;
import org.lwjgl.opengl.*;
import xyz.jadonfowler.minekraft.*;
import xyz.jadonfowler.minekraft.entity.*;
import com.nishu.utils.*;

public class World extends Screen {

    public Camera camera;
    
    public World(){
        initGL();
        init();
    }
    
    @Override public void dispose() {
        Display.destroy();
        System.exit(0);
    }

    @Override public void init() {
        camera = new Camera3D.CameraBuilder().setAspectRatio(Minekraft.WIDTH / Minekraft.HEIGHT).setRotation(0, 0, 0)
                .setPosition(0, 0, 0).setFieldOfView(Minekraft.FOV).build();
    }

    @Override public void initGL() {}

    @Override public void render() {}

    @Override public void update() {
        camera.updateKeys(32, 1);
        camera.updateMouse(1, 90, -90);
        if(Mouse.isButtonDown(0)){
            Mouse.setGrabbed(true);
        } else if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
            dispose();
        }
        EntityPlayer p = Minekraft.getInstance().thePlayer;
        camera.setRotation(p.pitch, p.yaw, 0);
        camera.setPos((float) p.pos.x, (float) p.pos.y,
                (float) p.pos.z);
    }
}
