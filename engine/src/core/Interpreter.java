package core;

import java.io.File;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.joml.Vector3f;

import entity.Focus;
import entity.Light;
import entity.Entity;
import entity.Planet;
import entity.Skybox;
import core.AssetImporter;
import opengl.Camera;
import opengl.Obj;
import opengl.ObjTexture;
import opengl.RawObj;


public class Interpreter {
	Scanner scan;
	ArrayList<String[]> physicsData = new ArrayList<String[]>();
	ArrayList<Entity> PhysicalObjects = new ArrayList<Entity>();
	ArrayList<Light> lights = new ArrayList<Light>();
	RawObj rawPlayerModel;
	ObjTexture playerTex;
	Focus player;
	ObjTexture skyboxTex;
	Skybox skybox;
	Skybox skybox2;
	Camera camera;
	Planet planet;

	private static int index = 0;


	public void loadMapData(String filePath, AssetImporter objLoader) {
		String line;
		try {
			scan = new Scanner(new File("res/scripts/"+filePath+".orbt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("Missing configuration file, failed to load : " + filePath);

		}

		while(scan.hasNextLine()) {
			line = scan.nextLine();
			if(line.length() == 0) {
				continue;
			}
			if(line.charAt(0) == '#') {
				continue;
			}

			String[] data = line.split(" ");
			if(data[0].equals("Light")) {
				float x = Float.parseFloat(data[1]);
				float y = Float.parseFloat(data[2]);
				float z = Float.parseFloat(data[3]);
				float r = Float.parseFloat(data[4]);
				float g = Float.parseFloat(data[5]);
				float b = Float.parseFloat(data[6]);
				lights.add( new Light(new Vector3f(x,y,z),new Vector3f(r,g,b)));
				continue;
			}
			if(data[0].equals("Skybox")) {
				RawObj raw = objLoader.loadToVao(Skybox.getVertexData(), Skybox.getIndicesData(), Skybox.getNormalData(), Skybox.getTextureData());
				ObjTexture skyTex = new ObjTexture(objLoader.loadTexture("textures/" + data[1]));
				float x = Float.parseFloat(data[2]);
				float y = Float.parseFloat(data[3]);
				float z = Float.parseFloat(data[4]);
				float rx = Float.parseFloat(data[5]);
				float ry = Float.parseFloat(data[6]);
				float rz = Float.parseFloat(data[7]);
				float sc = Float.parseFloat(data[8]);
				if(skybox == null) {
					skybox = new Skybox(new Obj(raw,skyTex),new Vector3f(x,y,z),rx,ry,rz,sc);
					continue;
				}else {
					skybox2 = new Skybox(new Obj(raw,skyTex),new Vector3f(x,y,z),rx,ry,rz,sc);
					continue;
				}
			}

			else if(data[0].equals("Object")) {
				RawObj raw = objLoader.loadObjModel("models/" + data[1]);
				ObjTexture texture = new ObjTexture(objLoader.loadTexture("textures/" + data[2]));
				float x = Float.parseFloat(data[3]);
				float y = Float.parseFloat(data[4]);
				float z = Float.parseFloat(data[5]);
				float rx = Float.parseFloat(data[6]);
				float ry = Float.parseFloat(data[7]);
				float rz = Float.parseFloat(data[8]);
				float sc = Float.parseFloat(data[9]);
				PhysicalObjects.add(new Entity(new Obj(raw,texture),new Vector3f(x,y,z),rx,ry,rz,sc));
				continue;

			}

			else if(data[0].equals("Phy")) {
				float mass = Float.parseFloat(data[1]);
				float xVelo = Float.parseFloat(data[2]);
				float yVelo = Float.parseFloat(data[3]);
				float zVelo = Float.parseFloat(data[4]);
				/*angular speed*/
				float angRotX = Float.parseFloat(data[5]);
				float angRotY = Float.parseFloat(data[6]);
				float angRotZ = Float.parseFloat(data[7]);
				physicsData.add(data);
				continue;

			}
			
			System.err.println("Unidentified type :  " + data[0]);


		}
	}


	public Scanner getScan() {
		return scan;
	}


	public ArrayList<String[]> getPhysicsData() {
		return physicsData;
	}



	public ArrayList<Entity> getPhysicalObjects() {
		return PhysicalObjects;
	}


	public ArrayList<Light> getLights() {
		return lights;
	}



	public ObjTexture getSkyboxTex() {
		return skyboxTex;
	}



	public Skybox getSkybox() {
		return skybox;
	}



	public Skybox getSkybox2() {
		return skybox2;
	}



	public Camera getCamera() {
		return camera;
	}







	public static int getIndex() {
		return index;
	}



	public void link() {
		for(int i = 0; i< PhysicalObjects.size(); i++) {
			String[] data = physicsData.get(i);
			if(data.length < 8 || PhysicalObjects.get(i) == null) {
				System.err.println("mismatch physics data in file!");
			}
			float mass = Float.parseFloat(data[1]);
			float xVelo = Float.parseFloat(data[2]);
			float yVelo = Float.parseFloat(data[3]);
			float zVelo = Float.parseFloat(data[4]);
			/*angular speed*/
			float angRotX = Float.parseFloat(data[5]);
			float angRotY = Float.parseFloat(data[6]);
			float angRotZ = Float.parseFloat(data[7]);
			PhysicalObjects.get(i).setMass(mass);
			PhysicalObjects.get(i).setVelocity(new Vector3f(xVelo,yVelo,zVelo));
			PhysicalObjects.get(i).setAngularVel(new Vector3f(angRotX,angRotY,angRotZ));

		}
	}


}













