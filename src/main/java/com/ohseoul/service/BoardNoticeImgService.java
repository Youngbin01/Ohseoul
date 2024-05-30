package com.ohseoul.service;

import com.ohseoul.entity.BoardNoticeImg;
import com.ohseoul.repository.BoardNoticeImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BoardNoticeImgService {
    @Value("${boardCommunityImgLocation}")
    private String boardCommunityImgLocation;

    private final BoardNoticeImgRepository boardNoticeImgRepository;
    private final FileService fileService;

    public void saveNoticeImg(BoardNoticeImg boardNoticeImg, MultipartFile multipartFile) throws Exception{
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(boardCommunityImgLocation,oriImgName, multipartFile.getBytes());
            imgUrl = "/images/board/"+imgName;

        }
        boardNoticeImg.updateNoticeImg(oriImgName,imgName,imgUrl);
        boardNoticeImgRepository.save(boardNoticeImg);
    }

    public void updateNoticeImg(Long noticeImgId, MultipartFile noticeImgFile) throws Exception{
        System.out.println("updateBoardImg 서비스+++boardImgId+"+noticeImgId);
        System.out.println("updateBoardImg 서비스+++boardImgFile+"+noticeImgFile);
        if(!noticeImgFile.isEmpty()){
            System.out.println("updateBoardImg 서비스++isEmpty++");
            BoardNoticeImg saveNoticeImg = (BoardNoticeImg) boardNoticeImgRepository.findById(noticeImgId)
                    .orElseThrow(EntityNotFoundException::new);
            System.out.println("isEmty 다음 +++++"+saveNoticeImg);

            if(!StringUtils.isEmpty(saveNoticeImg.getImgName())){
                System.out.println("updateBoardImg 서비스+StringUtils+++");
                fileService.deleteFile(boardCommunityImgLocation+"/"+saveNoticeImg.getImgName());
            }

            System.out.println("if문 나왔음 ");
            String oriImgName = noticeImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(boardCommunityImgLocation,oriImgName, noticeImgFile.getBytes());
            String imgUrl = "/images/board/"+imgName;
            saveNoticeImg.updateNoticeImg(oriImgName,imgName,imgUrl);

        }

    }

    public void deleteNoticeImg(Long noticeImgId,MultipartFile noticeImgFile) throws Exception{
        if (!noticeImgFile.isEmpty()){
            BoardNoticeImg saveNoticeImg = boardNoticeImgRepository.findById(noticeImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(saveNoticeImg.getImgName())){
                fileService.deleteFile(boardCommunityImgLocation+"/"+saveNoticeImg.getImgName());
            }
        }
    }


}
