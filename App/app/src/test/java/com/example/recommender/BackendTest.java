//package com.example.recommender;
//
//import static org.mockito.Mockito.*;
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.mock;
//
//import com.example.recommender.data.network.AuthService;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//import org.json.JSONObject;
//
//public class BackendTest {
//
//    private Controller controller;
//    private AuthService mockAuthService;
//    private API mockDatabaseAPI;
//    private API mockBookAPI;
//
//    @Before
//    public void setUp() {
//        mockDatabaseAPI = mock(API.class);
//        mockBookAPI = mock(API.class);
//        mockAuthService = mock(AuthService.class);
//
//        // Mock API endpoint and key
//        when(mockDatabaseAPI.getEndpoint()).thenReturn("https://mockapi.com");
//        when(mockDatabaseAPI.getKey()).thenReturn("test-api-key");
//
//        // Mock the Controller
//        controller = new Controller(mockAuthService);
//    }
//
//    @Test
//    public void testLogin_Success() throws Exception {
//        // Arrange
//        String username = "alice";
//        String password = "password123";
//        String jsonResponse = "{ \"success\": true, \"user_id\": \"1\", \"username\": \"alice\" }";
//
//        ArgumentCaptor<AuthService.AuthCallback> callbackCaptor = ArgumentCaptor.forClass(AuthService.AuthCallback.class);
//
//        controller.login(username, password);
//
//        verify(mockAuthService).login(eq(username), eq(password), callbackCaptor.capture());
//
//        callbackCaptor.getValue().onSuccess(jsonResponse);
//
//        assertEquals("123", Store.getInstance().getUserId());
//        assertEquals("alice", Store.getInstance().getUsername());
//    }
//
//    @Test
//    public void testLogin_Failure() throws Exception {
//        // Arrange
//        String username = "alice";
//        String password = "wrongpassword";
//
//        // Capture the callback argument
//        ArgumentCaptor<AuthService.AuthCallback> callbackCaptor = ArgumentCaptor.forClass(AuthService.AuthCallback.class);
//
//        // Act: Call login
//        controller.login(username, password);
//
//        // Verify that AuthService.login() was called
//        verify(mockAuthService).login(eq(username), eq(password), callbackCaptor.capture());
//
//        // Simulate a failed response
//        callbackCaptor.getValue().onFailure(new Exception("Invalid credentials"));
//
//        // Assert: Ensure Store was not updated
//        assertNull(Store.getInstance().getUserId());
//        assertNull(Store.getInstance().getUsername());
//    }
//}
