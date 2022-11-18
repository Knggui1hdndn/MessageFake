package com.example.messagefake;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class user implements Serializable {
    private String avata,UID;
    private String coverImage;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    private String email, name, password, phone, birthDay, sex;
    private boolean status;
    private byte[] av;
    private byte[] cI;
    private String string33, token,relationship;
    private List<String> friends;
    private List<String> messageWaiting;
    private List<String> archivedMessage;//tin nhăn lưu trữ
    private List<String> block;
    private List<String> blockMe;
    private String chat, Habitat, Address  ;
    private List<String> story;
    private List<String> interests;
    private List<String> Education;
    private List<String> Work,publicDetails,ShowInIntroduce,friendRequest ;



    public List<String> getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(List<String> friendRequest) {
        this.friendRequest = friendRequest;
    }

    public List<String> getShowInIntroduce() {
        return ShowInIntroduce;
    }

    public void setShowInIntroduce(List<String> showInIntroduce) {
        ShowInIntroduce = showInIntroduce;
    }

    public List<String> getPublicDetails() {
        return publicDetails;
    }

    public void setPublicDetails(List<String> publicDetails) {
        this.publicDetails = publicDetails;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public List<String> getStory() {
        return story;
    }

    public void setStory(List<String> story) {
        this.story = story;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }



    public String getCoverImage() {
        return coverImage;
    }

    public List<String> getMessageWaiting() {
        return messageWaiting;
    }

    public void setMessageWaiting(List<String> messageWaiting) {
        this.messageWaiting = messageWaiting;
    }

    public List<String> getArchivedMessage() {
        return archivedMessage;
    }

    public void setArchivedMessage(List<String> archivedMessage) {
        this.archivedMessage = archivedMessage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public byte[] getcI() {
        return cI;
    }

    public void setcI(byte[] cI) {
        this.cI = cI;
    }

    public List<String> getEducation() {
        return Education;
    }

    public void setEducation(List<String> education) {
        Education = education;
    }

    public List<String> getWork() {
        return Work;
    }

    public void setWork(List<String> work) {
        Work = work;
    }

    public String getHabitat() {
        return Habitat;
    }

    public void setHabitat(String habitat) {
        Habitat = habitat;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public user(String avata, String email, String name, String password, String phone, String birthDay, String sex, boolean status, String string33, String token, List<String> friends, List<String> messageWaiting, List<String> archivedMessage, List<String> block, List<String> blockMe,String UID) {
        this.avata = avata;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.birthDay = birthDay;
        this.sex = sex;
        this.status = status;
        this.string33 = string33;
        this.token = token;
        this.friends = friends;
        this.messageWaiting = messageWaiting;
        this.archivedMessage = archivedMessage;
        this.block = block;
        this.blockMe = blockMe;
        this.UID = UID;
    }



    public List<String> getBlockMe() {
        return blockMe;
    }

    public void setBlockMe(List<String> blockMe) {
        this.blockMe = blockMe;
    }

    public List<String> getBlock() {
        return block;
    }

    public void setBlock(List<String> block) {
        this.block = block;
    }

    public byte[] getAv() {
        return av;
    }

    public void setAv(byte[] av) {
        this.av = av;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public user() {
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getString33() {
        return string33;
    }

    public void setString33(String string33) {
        this.string33 = string33;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    @Override
    public String toString() {
        return "user{" +
                "avata='" + avata + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDay='" + birthDay + '\'' +
                ", sex='" + sex + '\'' +
                ", status=" + status +
                ", av=" + Arrays.toString(av) +
                ", string33='" + string33 + '\'' +
                ", token='" + token + '\'' +
                ", friends=" + friends +
                ", chat='" + chat + '\'' +
                '}';
    }
}
