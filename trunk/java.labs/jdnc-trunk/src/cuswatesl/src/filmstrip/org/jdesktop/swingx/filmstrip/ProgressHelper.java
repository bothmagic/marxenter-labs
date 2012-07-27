package org.jdesktop.swingx.filmstrip;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTargetAdapter;
import org.jdesktop.swingx.ImageSource;
import org.jdesktop.swingx.ImageSource.State;


public class ProgressHelper {

	private Animator anim;
	private Runnable repaintCallback;
	private ImageSource imageSource;
	
	void runCallback() {
		repaintCallback.run();
	}
	
	public ProgressHelper(Runnable repaintCallback, ImageSource imageSource) {
		this.repaintCallback = repaintCallback;
		this.imageSource = imageSource;
		this.anim = new Animator(Animator.INFINITE, new TimingTargetAdapter() {
			@Override
			public void timingEvent(float fraction) {
				runCallback();
			}
			@Override
			public void end() {
				runCallback();
			}
		} );
		
		imageSource.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				State state = ProgressHelper.this.imageSource.getState();
				switch (state) { 
				case ERROR:
				case DONE:
				case IDLE:
					anim.stop();
					break;
				case LOADING:
					anim.start();
					break;
				}
			}
		} );
		
		State state = ProgressHelper.this.imageSource.getState();
		switch (state) {
		case ERROR:
		case DONE:
		case IDLE:
			anim.stop();
			break;
		case LOADING:
			anim.start();
			break;
		}
	}
}
