package ir.Ali.hollowknightme.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ir.Ali.hollowknightme.controller.enemy.EnemyManager;
import ir.Ali.hollowknightme.controller.map.*;
import ir.Ali.hollowknightme.enums.game.GameStatus;
import ir.Ali.hollowknightme.controller.knight.KnightContactListener;
import ir.Ali.hollowknightme.controller.knight.KnightController;
import ir.Ali.hollowknightme.controller.knight.KnightManager;
import ir.Ali.hollowknightme.controller.game.CompositeContactListener;
import ir.Ali.hollowknightme.controller.enemy.EnemyContactListener;
import ir.Ali.hollowknightme.controller.enemy.EnemyControllerManager;
import ir.Ali.hollowknightme.factory.enemy.EnemyFactory;
import ir.Ali.hollowknightme.controller.camera.CameraShakeManager;
import ir.Ali.hollowknightme.controller.game.GameProgressManager;
import ir.Ali.hollowknightme.controller.game.GameRunManager;
import ir.Ali.hollowknightme.controller.game.GameStateManager;
import ir.Ali.hollowknightme.controller.screens.ScreenManager;
import ir.Ali.hollowknightme.model.enviroment.Environment;
import ir.Ali.hollowknightme.view.popup.*;
import ir.Ali.hollowknightme.model.knight.Knight;
import ir.Ali.hollowknightme.service.JsonSaveService;
import ir.Ali.hollowknightme.view.knight.KnightRenderer;
import ir.Ali.hollowknightme.view.enemy.EnemyAnimationLibrary;
import ir.Ali.hollowknightme.view.hud.HUDRenderer;
import ir.Ali.hollowknightme.view.map.DegradableWallAtlas;
import ir.Ali.hollowknightme.view.map.DegradableWallRenderer;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

import java.util.List;

public class GameScreen extends BaseScreen {

    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;
    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final MapManager mapManager;
    private final LoadMap loadMap;
    private final EnemyManager enemyManager;
    private final SpawnManager spawnManager;
    private final TransitionManager transitionManager;
    private final FadeManager fadeManager;
    private final ShapeRenderer fadeRenderer;
    private final Knight knight;
    private final KnightController knightController;
    private final HUDRenderer hudRenderer;

    private final KnightRenderer knightRenderer;
    private final KnightManager knightManager;
    private final GameRunManager gameRunManager;

    private final DegradableWallManager degradableWallManager;
    private final DegradableWallAtlas degradableWallAtlas;
    private final DegradableWallRenderer degradableWallRenderer;

    private PauseMenuPanel pauseMenuPanel;
    private InventoryPopup inventoryPopup;

    private float resultPanelTimer = 2f;
    private boolean resultPanelShown = false;
    private GameResultPanel gameResultPanel;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(19.2f, 10.8f, camera);
        batch = new SpriteBatch();
        world = new World(new Vector2(0, -10f), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        fadeManager = new FadeManager();
        fadeRenderer = new ShapeRenderer();

        mapManager = new MapManager();
        Environment environment = new Environment("Default");
        loadMap = new LoadMap(environment , mapManager);

        GameStateManager.getInstance().setCurrentEnvironment(environment);
        GameStateManager.getInstance().setGameStatus(GameStatus.PLAYING);

        String targetRoomPath = GameProgressManager.getInstance().getCurrentRoomPath();

        int targetRoomIndex = 0;
        var rooms = environment.getRooms();
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getMapPath().equals(targetRoomPath)) {
                targetRoomIndex = i;
                break;
            }
        }
        GameStateManager.getInstance().moveToRoom(targetRoomIndex);
        mapManager.loadMap(targetRoomPath);

        EnemyControllerManager controllerManager = new EnemyControllerManager();
        enemyManager = new EnemyManager(world, controllerManager, new EnemyAnimationLibrary());

        EnemyFactory enemyFactory = new EnemyFactory(world, controllerManager, enemyManager);
        spawnManager = new SpawnManager(enemyFactory);

        degradableWallManager = new DegradableWallManager(world);
        degradableWallAtlas = new DegradableWallAtlas();
        degradableWallRenderer = new DegradableWallRenderer(degradableWallManager, degradableWallAtlas);

        List<Body> initialBodies = PhysicsBuilder.buildPhysics(world, mapManager.getMapData(), degradableWallManager);
        Vector2 spawnPos = spawnManager.getPlayerSpawn(mapManager.getMapData() , true);
        GameStateManager.getInstance().updateSafePosition(spawnPos.x, spawnPos.y);

        knight = new Knight(world, spawnPos.x * MapManager.PPM, spawnPos.y * MapManager.PPM, 40f, 80f);
        knight.setCurrentHealth(GameProgressManager.getInstance().getCurrentHealth());
        knight.setSoul(GameProgressManager.getInstance().getCurrentSoul());

        knightManager = new KnightManager(knight , spawnManager , mapManager);
        gameRunManager = new GameRunManager(knightManager);

        KnightContactListener knightContactListener = new KnightContactListener(knightManager, degradableWallManager ,
            environment.getCurrentRoom().getMapData());
        knightContactListener.setGameRunManager(gameRunManager);

        knightController = new KnightController(knight, world, knightContactListener);
        knightManager.setKnightController(knightController);

        enemyManager.setTarget(knight);
        spawnManager.spawnEntities(mapManager.getMapData(), GameStateManager.getInstance().getCurrentRoomState());

        transitionManager = new TransitionManager(knightManager, mapManager, world, enemyManager,
            spawnManager, fadeManager, degradableWallManager);
        transitionManager.setWorldBodies(initialBodies);

        CompositeContactListener ccl = new CompositeContactListener();
        ccl.addListener(knightContactListener);
        ccl.addListener(transitionManager);
        ccl.addListener(new EnemyContactListener(knight, controllerManager , gameRunManager));
        world.setContactListener(ccl);

        knightRenderer = new KnightRenderer(knight);
        hudRenderer = new HUDRenderer(knight);

        GameProgressManager.getInstance().setCurrentRoomPath(targetRoomPath);
    }

    private void update(float delta) {
        GameStatus status = GameStateManager.getInstance().getGameStatus();

        if (status != GameStatus.PLAYING && status != GameStatus.GAME_OVER && status != GameStatus.WIN) return;
        if (resultPanelShown) return;

        GameProgressManager.getInstance().updateTime(delta);

        Vector2 beforePos = knight.getBody().getPosition().cpy();
        knightManager.processPendingRespawn();

        if (beforePos.dst(knight.getBody().getPosition()) > 2f) {
            float vW = viewport.getWorldWidth();
            float vH = viewport.getWorldHeight();
            float mW = mapManager.getMapWidth();
            float mH = mapManager.getMapHeight();

            float cX = (mW < vW) ? mW / 2f : Math.max(vW / 2f, Math.min(mW - vW / 2f, knight.getBody().getPosition().x));
            float cY = (mH < vH) ? mH / 2f : Math.max(vH / 2f, Math.min(mH - vH / 2f, knight.getBody().getPosition().y));

            camera.position.set(cX, cY, 0);
            camera.update();
        }
        world.step(1 / 60f, 6, 2);

        degradableWallManager.update();
        transitionManager.update();
        fadeManager.update(delta);

        if (!fadeManager.isBusy()) {
            knightController.update(delta);
            enemyManager.update(delta);
        }

        knightRenderer.update(delta);
        hudRenderer.update(delta);

        float vW = viewport.getWorldWidth();
        float vH = viewport.getWorldHeight();
        float mW = mapManager.getMapWidth();
        float mH = mapManager.getMapHeight();

        float cX = (mW < vW) ? mW / 2f : Math.max(vW / 2f, Math.min(mW - vW / 2f, knight.getBody().getPosition().x));
        float cY = (mH < vH) ? mH / 2f : Math.max(vH / 2f, Math.min(mH - vH / 2f, knight.getBody().getPosition().y));

        CameraShakeManager.getInstance().update(delta, camera, cX, cY);
        camera.update();
    }

    public void saveCurrentProgress() {
        GameProgressManager progress = GameProgressManager.getInstance();

        if (knight.getCurrentHealth() <= 0) {
            progress.setCurrentHealth(5);
        } else {
            progress.setCurrentHealth(knight.getCurrentHealth());
        }

        progress.setCurrentSoul(knight.getSoul());
        progress.setCurrentRoomPath(mapManager.getCurrentMapPath());

        var saveData = progress.generateSaveData();
        new JsonSaveService().save(saveData);
    }

    @Override
    public void render(float delta) {
        GameStatus status = GameStateManager.getInstance().getGameStatus();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (status == GameStatus.PLAYING) {
                pauseMenuPanel = new PauseMenuPanel();

                pauseMenuPanel.getResumeBtn().addClickListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        pauseMenuPanel.hide(() -> GameStateManager.getInstance().setGameStatus(GameStatus.PLAYING));
                    }
                });

                pauseMenuPanel.getSettingsBtn().addClickListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        SettingsPanel settingsPanel = new SettingsPanel();
                        settingsPanel.show(stage);
                    }
                });

                pauseMenuPanel.getCheatCodesBtn().addClickListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        GuidePanel guidePanel = new GuidePanel();
                        guidePanel.show(stage);
                    }
                });

                pauseMenuPanel.getSaveExitBtn().addClickListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        pauseMenuPanel.hide(() -> {
                            saveCurrentProgress();
                            ScreenManager.getInstance().setScreen(new MainMenuScreen());
                        });
                    }
                });

                pauseMenuPanel.show(stage);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (status == GameStatus.PLAYING) {
                inventoryPopup = new InventoryPopup();
                inventoryPopup.show(stage);
            } else if (status == GameStatus.PAUSED && inventoryPopup != null && inventoryPopup.getStage() != null) {
                inventoryPopup.hide();
                inventoryPopup = null;
            }
        }

        if (status == GameStatus.PLAYING || status == GameStatus.PAUSED || status == GameStatus.GAME_OVER || status == GameStatus.WIN) {

            stage.act(delta);

            if (status == GameStatus.PLAYING || ((status == GameStatus.GAME_OVER || status == GameStatus.WIN) && !resultPanelShown)) {
                update(delta);

                if (status == GameStatus.GAME_OVER || status == GameStatus.WIN) {
                    resultPanelTimer -= delta;
                    if (resultPanelTimer <= 0 && !resultPanelShown) {
                        resultPanelShown = true;

                        gameResultPanel = new GameResultPanel(status == GameStatus.WIN);
                        gameResultPanel.getMainMenuBtn().addClickListener(new ClickListener() {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                gameResultPanel.hide(() -> {
                                    saveCurrentProgress();
                                    ScreenManager.getInstance().setScreen(new MainMenuScreen());
                                });
                            }
                        });
                        gameResultPanel.show(stage);
                    }
                }
            }

            Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            mapManager.render(camera);

            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            knightRenderer.render(batch, delta);
            enemyManager.render(batch, delta);
            degradableWallRenderer.render(batch);

            batch.end();

            box2DDebugRenderer.render(world, camera.combined);
            renderFade();
            hudRenderer.render(batch);

            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudRenderer.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    private void renderFade() {
        if (!fadeManager.isBusy()) {
            return;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        fadeRenderer.setProjectionMatrix(camera.combined);
        fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        fadeRenderer.setColor(0f, 0f, 0f, fadeManager.getAlpha());

        fadeRenderer.rect(
            camera.position.x - viewport.getWorldWidth() / 2f,
            camera.position.y - viewport.getWorldHeight() / 2f,
            viewport.getWorldWidth(),
            viewport.getWorldHeight()
        );

        fadeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        mapManager.dispose();
        knightRenderer.dispose();
        hudRenderer.dispose();
        fadeRenderer.dispose();
        degradableWallAtlas.dispose();
    }
}
