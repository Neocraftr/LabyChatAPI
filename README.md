# LayChatAPI

API with easy to use events and methods to interact with the the LabyMod chat.

## Installation
Copy the `de.neocraftr.labychatapi` package in your LabyMod addon and add `de.neocraftr.labychatapi.transformer.LabyChatApiTransformer` as transformer class to yout addon.json

## Example
```java
LabyChatAPI chatAPI = LabyChatAPI.getInstance();
chatAPI.registerEvent(new LabyChatMessageEvent() {
    @Override
    public void onMessage(GameProfile sender, String message, PacketMessage rawPacket) {
        System.out.println("LabyChat message from "+sender.getName()+": "+message);
        
        // Echo back message
        chatAPI.sendMessage(sender.getId(), message);
    }
});
```