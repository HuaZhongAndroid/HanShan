package com.bm.tzj.activity;



/**
 * 
 *六边形 图片  参考地址  https://github.com/AlbertGrobas/PolygonImageView
 * 
 * 
 */
//public class TestSixShapeAc{
//
//	public class SampleActivity extends AppCompatActivity {
//
//	    @Override
//	    protected void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        setContentView(R.layout.activity_sample);
//
//	        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
//
//	        PolygonImageView kitty = (PolygonImageView) findViewById(R.id.kitty01);
//	        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
//
//	        PolygonImageView view = new PolygonImageView(this);
//	        view.setImageResource(R.drawable.cat07);
//
//	        view.addShadow(10f, 0f, 0f, Color.RED);
//	        view.addBorder(10, Color.WHITE);
//	        view.setCornerRadius(10);
//	        view.setVertices(16);
//
//	        view.setPolygonShape(new StarPolygonShape(0.8f, false));
//	        layout.addView(view, kitty.getLayoutParams());
//
//	        PolygonImageView view2 = new PolygonImageView(this);
//	        view2.setImageResource(R.drawable.cat01);
//	        view2.addShadow(15f, 0f, 0f, Color.BLACK);
//	        view2.addBorder(10, Color.WHITE);
//	        view2.setCornerRadius(10);
//	        view2.setVertices(24);
//
//	        view2.setPolygonShape(new StarPolygonShape(0.8f, true));
//
//	        layout.addView(view2, kitty.getLayoutParams());
//
//	        PolygonImageView view3 = new PolygonImageView(this);
//	        view3.setImageResource(R.drawable.cat04);
//	        view3.addShadowResource(10f, 0f, 7.5f, R.color.shadow);
//	        view3.addBorderResource(5, R.color.border);
//	        view3.setCornerRadius(2);
//	        view3.setVertices(5);
//
//	        view3.setPolygonShape(new PaperPolygonShape(-15, 25));
//	        layout.addView(view3, kitty.getLayoutParams());
//
//	    }
//	
//}
//	<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//		    xmlns:app="http://schemas.android.com/apk/res-auto"
//		    android:layout_width="match_parent"
//		    android:layout_height="match_parent"
//		    android:fitsSystemWindows="true">
//
//		    <android.support.v7.widget.Toolbar xmlns:local="http://schemas.android.com/apk/res-auto"
//		        android:id="@+id/toolbar"
//		        android:layout_width="match_parent"
//		        android:layout_height="wrap_content"
//		        android:background="?attr/colorPrimary"
//		        android:minHeight="?attr/actionBarSize"
//		        local:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">
//
//		        <net.grobas.view.PolygonImageView
//		            android:layout_width="?attr/actionBarSize"
//		            android:layout_height="?attr/actionBarSize"
//		            android:src="@drawable/cat01"
//		            app:poly_border="true"
//		            app:poly_border_color="@android:color/white"
//		            app:poly_border_width="3dp"
//		            app:poly_corner_radius="3"
//		            app:poly_vertices="6" />
//
//		    </android.support.v7.widget.Toolbar>
//
//		    <ScrollView
//		        android:layout_width="match_parent"
//		        android:layout_height="wrap_content"
//		        android:layout_below="@id/toolbar"
//		        android:fitsSystemWindows="true">
//
//		        <LinearLayout
//		            android:id="@+id/layout"
//		            android:layout_width="match_parent"
//		            android:layout_height="wrap_content"
//		            android:gravity="center_horizontal"
//		            android:orientation="vertical">
//
//		            <net.grobas.view.PolygonImageView
//		                android:id="@+id/kitty01"
//		                android:layout_width="250dp"
//		                android:layout_height="250dp"
//		                android:src="@drawable/cat07"
//		                app:poly_border="true"
//		                app:poly_border_color="@android:color/white"
//		                app:poly_border_width="5dp"
//		                app:poly_corner_radius="25"
//		                app:poly_rotation_angle="0"
//		                app:poly_shadow="true"
//		                app:poly_shadow_color="#f01"
//		                app:poly_vertices="6" />
//
//		            <net.grobas.view.PolygonImageView
//		                android:layout_width="250dp"
//		                android:layout_height="250dp"
//		                android:src="@drawable/cat03"
//		                app:poly_border="true"
//		                app:poly_border_color="#f01"
//		                app:poly_border_width="5dp"
//		                app:poly_corner_radius="5"
//		                app:poly_rotation_angle="0"
//		                app:poly_shadow="true"
//		                app:poly_vertices="0" />
//
//		            <net.grobas.view.PolygonImageView
//		                android:layout_width="250dp"
//		                android:layout_height="250dp"
//		                android:src="@drawable/cat01"
//		                app:poly_border="true"
//		                app:poly_border_color="@android:color/white"
//		                app:poly_border_width="15dp"
//		                app:poly_corner_radius="5"
//		                app:poly_rotation_angle="90"
//		                app:poly_shadow="true"
//		                app:poly_vertices="4" />
//
//		            <net.grobas.view.PolygonImageView
//		                android:layout_width="250dp"
//		                android:layout_height="250dp"
//		                android:src="@drawable/cat04"
//		                app:poly_border="true"
//		                app:poly_border_color="@android:color/white"
//		                app:poly_border_width="15dp"
//		                app:poly_corner_radius="5"
//		                app:poly_rotation_angle="90"
//		                app:poly_shadow="true"
//		                app:poly_vertices="6"/>
//
//		            <net.grobas.view.PolygonImageView
//		                android:layout_width="250dp"
//		                android:layout_height="250dp"
//		                android:src="@drawable/cat06"
//		                app:poly_border="true"
//		                app:poly_border_color="#33b5e5"
//		                app:poly_border_width="15dp"
//		                app:poly_corner_radius="5"
//		                app:poly_rotation_angle="0"
//		                app:poly_shadow="true"
//		                app:poly_vertices="8" />
//
//		            <net.grobas.view.PolygonImageView
//		                android:layout_width="250dp"
//		                android:layout_height="250dp"
//		                android:src="@drawable/cat05"
//		                app:poly_border="true"
//		                app:poly_border_color="#33b5e5"
//		                app:poly_border_width="5dp"
//		                app:poly_corner_radius="15"
//		                app:poly_rotation_angle="0"
//		                app:poly_shadow="true"
//		                app:poly_vertices="8" />
//
//		        </LinearLayout>
//		    </ScrollView>
//		</RelativeLayout>
//
//
//	
//	
//	
//	