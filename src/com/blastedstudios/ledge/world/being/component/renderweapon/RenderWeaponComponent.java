package com.blastedstudios.ledge.world.being.component.renderweapon;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.ledge.world.being.Being.BodyPart;
import com.blastedstudios.ledge.world.being.component.AbstractComponent;
import com.blastedstudios.ledge.world.weapon.Melee;

@PluginImplementation
public class RenderWeaponComponent extends AbstractComponent {
	private transient Sprite weaponSprite;

	@Override public void render(float dt, SpriteBatch spriteBatch, GDXRenderer gdxRenderer, boolean facingLeft) {
		if(gdxRenderer != null && being.getEquippedWeapon() != null){
			if(weaponSprite == null){
				weaponSprite = new Sprite(gdxRenderer.getTexture(being.getEquippedWeapon().getResource() + ".png"));
				weaponSprite.setScale(Properties.getFloat("ragdoll.custom.scale"));
			}
			Vector2 position = being.getRagdoll().getWeaponCenter();
			float rotation = being.getRagdoll().getBodyPart(BodyPart.lArm).getAngle();
			if(being.getEquippedWeapon() instanceof Melee){
				Melee weapon = ((Melee)being.getEquippedWeapon());
				position = weapon.getBody().getWorldCenter();
				rotation = weapon.getBody().getAngle();
			}
			weaponSprite.setPosition(position.x - weaponSprite.getWidth()/2f, position.y - weaponSprite.getHeight()/2f);
			weaponSprite.setRotation((float)Math.toDegrees(rotation));
			if(facingLeft)
				weaponSprite.flip(false, true);
			weaponSprite.draw(spriteBatch);
			if(facingLeft)
				weaponSprite.flip(false, true);
		}
	}

	@Override public void setCurrentWeapon(int currentWeapon, World world){
		weaponSprite = null;
	}
}
