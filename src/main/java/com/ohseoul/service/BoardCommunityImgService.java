package com.ohseoul.service;

import com.ohseoul.entity.BoardCommunityImg;
import com.ohseoul.repository.BoardCommunityImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardCommunityImgService {

    @Value("${boardCommunityImgLocation}")
    private String boardCommunityImgLocation;

    private final BoardCommunityImgRepository boardCommunityImgRepository;
    private final FileService fileService;

    public void saveCommunityImg(BoardCommunityImg boardCommunityImg, MultipartFile multipartFile) throws Exception{
        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(boardCommunityImgLocation,oriImgName, multipartFile.getBytes());
            imgUrl = "/images/board/"+imgName;

        }
        boardCommunityImg.updateBoardImg(oriImgName,imgName,imgUrl);
        boardCommunityImgRepository.save(boardCommunityImg);
    }

    public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception{
        System.out.println("updateBoardImg 서비스+++boardImgId+"+boardImgId);
        System.out.println("updateBoardImg 서비스+++boardImgFile+"+boardImgFile);
        if(!boardImgFile.isEmpty()){
            System.out.println("updateBoardImg 서비스++isEmpty++");
            BoardCommunityImg saveBoardImg = (BoardCommunityImg) boardCommunityImgRepository.findById(boardImgId)
                                                                                            .orElseThrow(EntityNotFoundException::new);
            System.out.println("isEmty 다음 +++++"+saveBoardImg);

            if(!StringUtils.isEmpty(saveBoardImg.getImgName())){
                System.out.println("updateBoardImg 서비스+StringUtils+++");
                fileService.deleteFile(boardCommunityImgLocation+"/"+saveBoardImg.getImgName());
            }

            System.out.println("if문 나왔음 ");
            String oriImgName = boardImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(boardCommunityImgLocation,oriImgName, boardImgFile.getBytes());
            String imgUrl = "/images/board/"+imgName;
            saveBoardImg.updateBoardImg(oriImgName,imgName,imgUrl);

        }

    }

    public void deleteBoardImg(Long boardImgId,MultipartFile boardImgFile) throws Exception{
        if (!boardImgFile.isEmpty()){
            BoardCommunityImg saveBoardImg = boardCommunityImgRepository.findById(boardImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(saveBoardImg.getImgName())){
                fileService.deleteFile(boardCommunityImgLocation+"/"+saveBoardImg.getImgName());
            }
        }
    }

}
