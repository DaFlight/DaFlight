package me.dags.daflight.handler;

import me.dags.daflight.DaFlight;
import me.dags.daflight.MCHooks;
import me.dags.daflight.util.Rotation;
import me.dags.daflight.util.Vector3d;

/**
 * @author dags <dags@dags.me>
 */
public class MovementHandler {

    private final DaFlight daFlight;

    private float moveForward = 0F;
    private float moveStrafe = 0F;
    float maxFlySpeed = 10F;
    float maxWalkSpeed = 10F;
    boolean flying = false;
    boolean sprinting = false;
    boolean flyBoosting = false;
    boolean sprintBoosting = false;

    public MovementHandler(DaFlight daFlight) {
        this.daFlight = daFlight;
    }

    public void applyMovement(Vector3d direction, Rotation rotation) {
        if (flying && !DaFlight.instance().config().disabled) {
            moveFlying(direction, rotation);
        } else if (sprinting && !daFlight.config().disabled) {
            moveSprinting(direction, rotation);
        }
    }

    public void reset() {
        flying = false;
        sprinting = false;
        flyBoosting = false;
        sprintBoosting = false;
        maxFlySpeed = DaFlight.instance().isSinglePlayer() ? 100000F : 1F;
        maxWalkSpeed = DaFlight.instance().isSinglePlayer() ? 100000F : 1F;
    }

    public boolean flying() {
        return flying;
    }

    public boolean sprinting() {
        return sprinting;
    }

    public boolean disableFov() {
        return DaFlight.instance().config().disableFov && flying;
    }

    public boolean disableViewBob() {
        return flying;
    }

    public float jump(float normal) {
        if (sprinting && !daFlight.config().disabled) {
            float boost = sprintBoosting ? daFlight.config().sprintBoost : 1F;
            float speed = Math.min(daFlight.config().sprintSpeed * daFlight.config().jumpModifier * boost, maxWalkSpeed * 5);
            return normal * speed;
        }
        return normal;
    }

    public void setMovementInput(float moveForward, float moveStrafe) {
        this.moveForward = moveForward;
        this.moveStrafe = moveStrafe;
    }

    private void moveFlying(Vector3d heading, Rotation rotation) {
        double strafeMod = daFlight.config().strafeModifier;
        double ascendMod = daFlight.config().verticalModifier;
        double boost = flyBoosting ? daFlight.config().flyBoost : 1D;
        double speed = Math.min(daFlight.config().flySpeed * boost, maxFlySpeed);

        double pitch = Math.toRadians(rotation.getPitch());
        double yaw = Math.toRadians(rotation.getYaw());
        double dx = -Math.sin(yaw);
        double dz = Math.cos(yaw);

        heading.set(dx * moveForward, 0, dz * moveForward);
        heading.add(dz * moveStrafe * strafeMod, 0, -dx * moveStrafe * strafeMod);

        if (MCHooks.Game.inGameHasFocus()) {
            if (DaFlight.instance().inputHandler().flyUpBind.keyHeld()) {
                heading.add(0, ascendMod, 0);
            }

            if (DaFlight.instance().inputHandler().flyDownBind.keyHeld()) {
                heading.add(0, -ascendMod, 0);
            }
        }

        if (daFlight.config().flight3D) {
            double vy = -Math.sin(pitch);
            double hy = Math.abs(Math.cos(pitch));
            heading.mult(hy, 1, hy);
            heading.add(0, vy * moveForward * ascendMod, 0);
        }

        heading.norm();
        heading.mult(speed);
    }

    private void moveSprinting(Vector3d heading, Rotation rotation) {
        double strafeMod = daFlight.config().strafeModifier;
        double boost = sprintBoosting ? daFlight.config().sprintBoost : 1F;
        double speed = Math.min(daFlight.config().sprintSpeed * boost, maxWalkSpeed);

        double rads = Math.toRadians(rotation.getYaw());
        double dx = -Math.sin(rads);
        double dz = Math.cos(rads);

        heading.add(dx * moveForward, 0, dz * moveForward);
        heading.add(dz * moveStrafe * strafeMod, 0, -dx * moveStrafe * strafeMod);
        heading.mult(speed, 1, speed);
    }
}
