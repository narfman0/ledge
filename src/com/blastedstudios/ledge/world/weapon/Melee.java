package com.blastedstudios.ledge.world.weapon;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.FileUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;
import com.blastedstudios.ledge.physics.ragdoll.IRagdoll;
import com.blastedstudios.ledge.world.WorldManager;
import com.blastedstudios.ledge.world.being.Being;
import com.blastedstudios.ledge.world.being.Being.BodyPart;

public class Melee extends Weapon {
	private static final float MIN_DAMAGE = Properties.getFloat("melee.damage.min", .005f);
	private static final long serialVersionUID = 1L;
	private float width, height, offsetX, offsetY, density;
	private String bodyPath = "";
	private transient Body body;
	private transient Being owner;
	private transient long lastAttack;
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public float getDensity() {
		return density;
	}
	
	public Being getOwner() {
		return owner;
	}

	public Body getBody() {
		return body;
	}

	public String getBodyPath() {
		return bodyPath;
	}
	
	@Override public void activate(World world, IRagdoll ragdoll, Being owner) {
		this.owner = owner;
		Vector2 position = ragdoll.getWeaponCenter().cpy().add(offsetX, offsetY);
		if(bodyPath != null && !bodyPath.equals("")){
			FileHandle handle = Gdx.files.internal(bodyPath);
			try {
				GDXGroupExportStruct struct = (GDXGroupExportStruct) FileUtil.getSerializer(handle).load(handle);
				Map<String,Body> returnStruct = struct.instantiate(world, position);
				body = returnStruct.values().iterator().next();
				Filter filter = new Filter();
				filter.categoryBits = owner.getCat();
				filter.maskBits = owner.getMask();
				for(Fixture fixture : body.getFixtureList())
					fixture.setFilterData(filter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			body = PhysicsHelper.createRectangle(world, width, height, position, BodyType.DynamicBody, 
					.2f, .5f, density, owner.getMask(), owner.getCat(), (short)0);
		body.setUserData(this);
		if(Properties.getBool("melee.useweld", true)){
			WeldJointDef def = new WeldJointDef();
			def.initialize(body, ragdoll.getBodyPart(BodyPart.lHand), ragdoll.getBodyPart(BodyPart.lHand).getWorldCenter());
			world.createJoint(def);
		}else{
			RevoluteJointDef def = new RevoluteJointDef();
			def.enableLimit = true;
			def.lowerAngle = -.5f;
			def.upperAngle = .01f;
			def.initialize(body, ragdoll.getBodyPart(BodyPart.lHand), ragdoll.getBodyPart(BodyPart.lHand).getWorldCenter());
			world.createJoint(def);
		}
	}
	
	@Override public void deactivate(World world) {
		if(body != null)
			world.destroyBody(body);
		else
			Gdx.app.error("Melee.deactivate", "Want to destroy weapon body, but body null! " + this);
	}
	
	public static double impulseToDamageScalar(float impulse, float dt){
		//TODO: use dt here, as it matters greatly. Probably divide impulse 
		//by it as long dts produce huge impulse, potentially
		return -.1 + .4 * Math.log(600 * impulse);
	}

	@Override public String toString(){
		return "[Melee name:" + name + " dmg: " + getDamage() + "]";
	}

	@Override public void beginContact(WorldManager worldManager, Being target, 
			Fixture hit, Contact contact, float i) {
		if(!owner.isFriendly(target.getFaction())){
			lastAttack = System.currentTimeMillis() - 1000l;//Smaller to make attack frequency better
			float meleeDmg = MIN_DAMAGE;
			if(i > Properties.getFloat("melee.contact.impulse.threshold", 2f))
				meleeDmg = (float)Melee.impulseToDamageScalar(i/10f, Gdx.graphics.getRawDeltaTime());
			worldManager.processHit(getDamage() * meleeDmg, target, owner, hit, contact.getWorldManifold().getNormal());
		}
	}

	@Override public long getMSSinceAttack() {
		return System.currentTimeMillis() - lastAttack;
	}

	@Override public boolean canAttack() {
		return true;
	}
}
