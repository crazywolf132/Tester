package com.fdoom.entity;

import java.util.ArrayList;
import java.util.List;
import com.fdoom.Game;
import com.fdoom.InputHandler;
import com.fdoom.entity.particle.TextParticle;
import com.fdoom.gfx.Color;
import com.fdoom.item.FurnitureItem;
import com.fdoom.item.ResourceItem;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.tile.HardRockTile;
import com.fdoom.level.tile.StairsTile;
import com.fdoom.level.tile.Tile;
import com.fdoom.level.tile.WaterTile;
import com.fdoom.sound.Sound;

public class TNT extends Furniture {
    public boolean on = false;
    public int ticks = 0;
    private final int range = 4;
    private final int dmg = 20;
    private final int sectodetonation = 5;

    public TNT() {
        super("TNT");
        col = Color.get(-1, 110,500, 480);
        sprite = 7;
    }

    public void tick() {
        super.tick();

        int xt = x >> 4;
        int yt = y >> 4;
/*
        boolean u = level.getTile(xt, yt - 1) == mod_redstone.wire;
        boolean d = level.getTile(xt, yt + 1) == mod_redstone.wire;
        boolean l = level.getTile(xt - 1, yt) == mod_redstone.wire;
        boolean r = level.getTile(xt + 1, yt) == mod_redstone.wire;

        List<RedstoneTile> connect = new ArrayList<RedstoneTile>();
        if(u) {
            connect.add((RedstoneTile)level.getTile(xt, yt - 1));
        }
        if(d) {
            connect.add((RedstoneTile)level.getTile(xt, yt + 1));
        }
        if(l) {
            connect.add((RedstoneTile)level.getTile(xt - 1, yt));
        }
        if(r) {
            connect.add((RedstoneTile)level.getTile(xt + 1, yt));
        }
        if(connect.size() > 0) {
            for(int i = 0; i < connect.size(); i++) {
                if(connect.get(i).signal) {
                    on = true;
                }
            }
        }*/

        if(on) {
            if(ticks == 0) {
                this.x += 1;
                this.y += 1;
                Sound.test.play();
            }
            ticks++;

            if(ticks % 60 == 0 && ticks != sectodetonation*60) {
                level.add(new TextParticle(String.valueOf(sectodetonation-ticks/60), xt * 16 + 8, yt * 16 + 8, Color.get(-1, 500, 500, 500)));
                Sound.test.play();
            }
            if(ticks > sectodetonation*60) {
                ticks = 0;
                explode(range + random.nextInt(1));
            }
        }
    }

    public boolean use(Player player, int attackDir) {
        if (!on) {
            on = true;
            this.x += 1;
            this.y += 1;
            Sound.test.play();
        }
        return true;
    }

    public void explode(int r) {
        Player player = null;
        Sound.playerDeath.play();
        List<Entity> result = new ArrayList<Entity>();
        result = this.level.getEntities(this.x-range*16, this.y-16*range, this.x+16*range, this.y+16*range);
        for (int i3=0;i3<result.size();i3++) {
            if (result.get(i3) instanceof TNT) {
                if(!((TNT)result.get(i3)).on) {
                    ((TNT)result.get(i3)).on = true;
                    ((TNT)result.get(i3)).explode(r);
                }
            } else
            if (result.get(i3) instanceof Mob) {
                Mob playa = (Mob) result.get(i3);

                level.remove(playa);
                playa.x += random.nextInt(100)-50;
                playa.y += random.nextInt(100)-50;
                level.add(playa);


                if (result.get(i3) instanceof Player) {
                    player = (Player)result.get(i3);
                } else {
                    playa.hurt(playa, dmg, playa.dir);
                }
            } else
            if(result.get(i3) instanceof Furniture) {
                if (!(result.get(i3) instanceof Workbench)){
                    Furniture furniture = (Furniture) result.get(i3);
                    this.level.add(new ItemEntity(new FurnitureItem(furniture), furniture.x+random.nextInt(100)-50, furniture.y+random.nextInt(100)-50));
                    furniture.remove();
                }
            }
        }
        if(player != null) {
            player = new Player(new Game(), new InputHandler(new Game()));
        }
        for (int i=-r;i<=r;i++) {
            for (int i2=-r;i2<=r;i2++) {
                int a = i; int b = i2;
                if(a < 0) a = -a;
                if(b < 0) b = -b;
                int l = Math.round((float)Math.sqrt((a) + (b)));
                if(l+1 < r && level.depth < 1) {
                    Tile tile = this.level.getTile(this.x/16+i, this.y/16+i2);
                    if(tile != null && (!(tile instanceof StairsTile)) &&  (!(tile instanceof HardRockTile))) {
                        tile.hurt(level, this.x/16+random.nextInt(8)-4, this.y/16+random.nextInt(8)-4, player, 100, 0);
                    }
                    if(random.nextInt(5)==0 && (!(tile instanceof StairsTile)) &&  (!(tile instanceof HardRockTile))){
                        this.level.setTile(this.x/16+i, this.y/16+i2, Tile.hole, 0);
                        level.add(new ItemEntity(new ResourceItem(Resource.dirt), x+random.nextInt(100)-50, y+random.nextInt(100)-50));
                    } else {
                        if ( (!(tile instanceof WaterTile)) && (!(tile instanceof StairsTile)) &&  (!(tile instanceof HardRockTile))) {
                            this.level.setTile(this.x/16+i, this.y/16+i2, Tile.dirt, 0);
                        }
                    }
                }
            }
        }
        this.on = false;
        this.remove();
    }

    public int getLightRadius() {
        if (on) return 10;
        return 0;
    }
}