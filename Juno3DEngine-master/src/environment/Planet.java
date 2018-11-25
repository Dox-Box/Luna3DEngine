package environment;

import org.joml.Vector3f;
import graphicsEngine.Obj;

public class Planet extends PhysicsObject {
	static final float bigG =  (float) (6.674 * Math.exp(10));

	public Planet(Obj texturedObj, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(texturedObj, position, rotX, rotY, rotZ, scale);

	}

	
	
	
	public void setPlanetRotation(Vector3f vec) {
		super.setAngularVel(vec);
	}

        public float getGravAccel(float distance){
                return (bigG * this.getMass())/ (distance*distance);
        }
}