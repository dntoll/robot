package daniel.robot.glWindow;

import java.util.List;

import javax.media.nativewindow.NativeSurface;
import javax.media.opengl.GL;
import javax.media.opengl.GLAnimatorControl;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilitiesImmutable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawable;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.GLRunnable;

import daniel.robot.glWindow.controller.MetaController;
import daniel.robot.glWindow.view.MetaView;


/*
 * This class adapts so the controller can be updated and initiated
 * */
public class MVCAdapter implements GLAutoDrawable {

	private MetaController controller;
	private MetaView view;

	public MVCAdapter(MetaController controller, MetaView view) {
		this.controller = controller;
		this.view = view;
	}

	@Override
	public void setRealized(boolean realized) {
		view.setRealized(realized);
	}

	@Override
	public boolean isRealized() {
		// TODO Auto-generated method stub
		return view.isRealized();
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return view.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return view.getHeight();
	}

	@Override
	public boolean isGLOriented() {
		// TODO Auto-generated method stub
		return view.isGLOriented();
	}

	@Override
	public void swapBuffers() throws GLException {
		view.swapBuffers();

	}

	@Override
	public GLCapabilitiesImmutable getChosenGLCapabilities() {
		// TODO Auto-generated method stub
		return view.getChosenGLCapabilities();
	}

	@Override
	public GLProfile getGLProfile() {
		// TODO Auto-generated method stub
		return view.getGLProfile();
	}

	@Override
	public NativeSurface getNativeSurface() {
		// TODO Auto-generated method stub
		return view.getNativeSurface();
	}

	@Override
	public long getHandle() {
		// TODO Auto-generated method stub
		return view.getHandle();
	}

	@Override
	public GLDrawableFactory getFactory() {
		// TODO Auto-generated method stub
		return view.getFactory();
	}

	@Override
	public GLDrawable getDelegatedDrawable() {
		// TODO Auto-generated method stub
		return view.getDelegatedDrawable();
	}

	@Override
	public GLContext getContext() {
		// TODO Auto-generated method stub
		return view.getContext();
	}

	@Override
	public GLContext setContext(GLContext newCtx, boolean destroyPrevCtx) {
		// TODO Auto-generated method stub
		return view.setContext(newCtx, destroyPrevCtx);
	}

	@Override
	public void addGLEventListener(GLEventListener listener) {
		view.addGLEventListener(listener);

	}

	@Override
	public void addGLEventListener(int index, GLEventListener listener)
			throws IndexOutOfBoundsException {
		view.addGLEventListener(index, listener);

	}

	@Override
	public int getGLEventListenerCount() {
		// TODO Auto-generated method stub
		return view.getGLEventListenerCount();
	}

	@Override
	public GLEventListener getGLEventListener(int index)
			throws IndexOutOfBoundsException {
		// TODO Auto-generated method stub
		return view.getGLEventListener(index);
	}

	@Override
	public boolean getGLEventListenerInitState(GLEventListener listener) {
		// TODO Auto-generated method stub
		return view.getGLEventListenerInitState(listener);
	}

	@Override
	public void setGLEventListenerInitState(GLEventListener listener,
			boolean initialized) {
		// TODO Auto-generated method stub
		view.setGLEventListenerInitState(listener, initialized);
	}

	@Override
	public GLEventListener disposeGLEventListener(GLEventListener listener,
			boolean remove) {
		// TODO Auto-generated method stub
		return view.disposeGLEventListener(listener, remove);
	}

	@Override
	public GLEventListener removeGLEventListener(GLEventListener listener) {
		// TODO Auto-generated method stub
		return view.removeGLEventListener(listener);
	}

	@Override
	public void setAnimator(GLAnimatorControl animatorControl)
			throws GLException {
		
		view.setAnimator(animatorControl);
	}

	@Override
	public GLAnimatorControl getAnimator() {
		// TODO Auto-generated method stub
		return view.getAnimator();
	}

	@Override
	public Thread setExclusiveContextThread(Thread t) throws GLException {
		// TODO Auto-generated method stub
		return view.setExclusiveContextThread(t);
	}

	@Override
	public Thread getExclusiveContextThread() {
		// TODO Auto-generated method stub
		return view.getExclusiveContextThread();
	}

	@Override
	public boolean invoke(boolean wait, GLRunnable glRunnable) {
		// TODO Auto-generated method stub
		return view.invoke(wait, glRunnable);
	}

	@Override
	public boolean invoke(boolean wait, List<GLRunnable> glRunnables) {
		// TODO Auto-generated method stub
		return view.invoke(wait, glRunnables);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		view.destroy();
	}

	@Override
	public void display() {
		
		controller.update();
		view.display();
	}

	@Override
	public void setAutoSwapBufferMode(boolean enable) {
		view.setAutoSwapBufferMode(enable);

	}

	@Override
	public boolean getAutoSwapBufferMode() {
		// TODO Auto-generated method stub
		return view.getAutoSwapBufferMode();
	}

	@Override
	public void setContextCreationFlags(int flags) {
		view.setContextCreationFlags(flags);
	}

	@Override
	public int getContextCreationFlags() {
		// TODO Auto-generated method stub
		return view.getContextCreationFlags();
	}

	@Override
	public GLContext createContext(GLContext shareWith) {
		// TODO Auto-generated method stub
		return view.createContext(shareWith);
	}

	@Override
	public GL getGL() {
		// TODO Auto-generated method stub
		return view.getGL();
	}

	@Override
	public GL setGL(GL gl) {
		// TODO Auto-generated method stub
		return view.setGL(gl);
	}

	@Override
	public Object getUpstreamWidget() {
		// TODO Auto-generated method stub
		return view.getUpstreamWidget();
	}

}
