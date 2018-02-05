package name.eskildsen.zoneminder.internal;

import java.nio.ByteBuffer;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Request.ContentListener;
import org.eclipse.jetty.client.api.Request.FailureListener;
import org.eclipse.jetty.client.api.Request.SuccessListener;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BufferingResponseListener;

import name.eskildsen.zoneminder.IZoneMinderResponse;


public class ZoneMinderRequestHandler//extends BufferingRequestListener
implements SuccessListener, FailureListener, ContentListener { 

	private org.eclipse.jetty.client.api.Request request;

	@Override
	public void onContent(Request request, ByteBuffer content) {
		System.out.println("onContent (Request)...");

		
	}

	@Override
	public void onFailure(Request request, Throwable failure) {
		this.request = request;
		System.out.println("onFailure (Request)...");
		
	}

	@Override
	public void onSuccess(Request request) {
		this.request = request;
		System.out.println("onSuccess (Request)...");
		
	}
}
